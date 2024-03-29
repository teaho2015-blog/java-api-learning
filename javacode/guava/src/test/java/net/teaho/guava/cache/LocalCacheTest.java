package net.teaho.guava.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.junit.Test;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 *
 * @author teaho2015@gmail.com
 * @date 2024-03
 */
public class LocalCacheTest {

    private int CACHE_MAXIMUM_SIZE = 200 * 10000;
    private static final CacheValue<InMemoryItem> emptyCacheValue = new CacheValue<>(null, 0);

    @Test
    public void testCacheLoad() {

        LoadingCache<CacheKey, CacheValue<InMemoryItem>> cache = CacheBuilder
            .newBuilder()
            .recordStats()
            .maximumSize(CACHE_MAXIMUM_SIZE)
            .expireAfterWrite(30 * 60, TimeUnit.SECONDS)
            .build(new LoadingCacheLoader());

        for (int i = 0; i < 50000; i++) {
            int val = ThreadLocalRandom.current().nextInt(0, 50000);

            try {
                CacheValue<InMemoryItem> item = cache.get(new CacheKey("" + val, "hashStr"));
                System.out.println("i:" + i + ", " + item.getV().id);
            } catch (ExecutionException e) {
                System.out.println("exception" + e.getMessage());
            }
        }

    }

    /**
     * test load all
     */
    @Test
    public void testCacheAll() throws InterruptedException {
        LoadingCache<CacheKey, CacheValue<InMemoryItem>> cache = CacheBuilder
            .newBuilder()
            .maximumSize(CACHE_MAXIMUM_SIZE)
            .concurrencyLevel(100)
            .expireAfterWrite(30 * 60, TimeUnit.SECONDS)
            .build(new LoadingCacheLoader());
        ExecutorService esInit = Executors.newFixedThreadPool(5);
        ExecutorService es = Executors.newFixedThreadPool(5);

        //1. init
        IntStream.range(0, 20 * 10000).parallel().forEach(operand -> {
            try {
                CacheValue<InMemoryItem> item = cache.get(new CacheKey("" + operand, "hashStr"));
                System.out.println("i:" + operand + ", " + item.getV().id);
            } catch (ExecutionException e) {
                System.err.println("err" + e.getMessage());
            }
        });

        // 500 latency
        for (int j = 0; j < 2000; j++) {
            es.submit(() -> {
                long start = Instant.now().toEpochMilli();
                for (int i = 0; i < 500; i++) {
                    int key = ThreadLocalRandom.current().nextInt(0, 30 * 10000);
                    try {
                        List<CacheKey> list = new ArrayList<>();
                        list.add(new CacheKey("" + key, "hashStr"));
                        Map<CacheKey, CacheValue<InMemoryItem>> item = cache.getAll(list);
                        //                    System.out.println("random get key:" + key + ", " + item.getV().id);
                    } catch (ExecutionException e) {
                        System.err.println("err" + e.getMessage());

                    }
                }
                System.out.println("random get " + (Instant.now().toEpochMilli() - start));
            });
        }

        TimeUnit.SECONDS.sleep(20L);
    }

    @Test
    public void testCacheLoadOne() {

        {
            LoadingCache<CacheKey, CacheValue<InMemoryItem>> cache = CacheBuilder
                .newBuilder()
                .recordStats()
                .maximumSize(CACHE_MAXIMUM_SIZE)
                .expireAfterWrite(30 * 60, TimeUnit.SECONDS)
                .build(new LoadingCacheLoader());

            int val = ThreadLocalRandom.current().nextInt(0, 50000);

            try {
                CacheValue<InMemoryItem> item = cache.get(new CacheKey("" + val, "hashStr"));

                System.out.println("random get key:" + val + ", " + item.getV().id);
            } catch (ExecutionException e) {
                System.out.println("exception" + e.getMessage());
            }
        }

        {
            Cache<CacheKey, CacheValue<InMemoryItem>> cache = CacheBuilder.newBuilder()
                .maximumSize(CACHE_MAXIMUM_SIZE)
                .expireAfterWrite(30 * 60, TimeUnit.SECONDS)
                .build();

            int val = ThreadLocalRandom.current().nextInt(0, 50000);

            try {
                CacheValue<InMemoryItem> item = cache.get(new CacheKey("" + val, "hashStr"), () -> emptyCacheValue);

                System.out.println("random get key:" + val + ", " + item.getV().id);
            } catch (ExecutionException e) {
                System.out.println("exception" + e.getMessage());
            }
        }

    }


    static class LoadingCacheLoader extends CacheLoader<CacheKey, CacheValue<InMemoryItem>> {

        private static AtomicInteger integer = new AtomicInteger(0);

        private static ExecutorService es = Executors.newFixedThreadPool(10);

        @Override
        public CacheValue<InMemoryItem> load(CacheKey cacheKey) {
            return loadOne(cacheKey);
        }

        @Override
        public Map<CacheKey, CacheValue<InMemoryItem>> loadAll(Iterable<? extends CacheKey> keys) {

            int batchSize = 4;
            Map<CacheKey, CacheValue<InMemoryItem>> map = new HashMap<>();
            Iterator<? extends CacheKey> iterator = keys.iterator();

            List<Future<Map<CacheKey, CacheValue<InMemoryItem>>>> futures = null;
            List<Callable<Map<CacheKey, CacheValue<InMemoryItem>>>> callList = new ArrayList<>();

            while (iterator.hasNext()) {
                List<CacheKey> itemBatch = new ArrayList<>();
                for (int i = 0; i < batchSize && iterator.hasNext(); i++) {
                    CacheKey key = iterator.next();
                    itemBatch.add(key);
                    // init, not to load again if failure.
                    map.put(key, emptyCacheValue);
                }
                Callable<Map<CacheKey, CacheValue<InMemoryItem>>> batchResCall = () -> itemBatch.stream()
                    .collect(Collectors.toMap(cacheKey -> cacheKey, this::loadOne));
                callList.add(batchResCall);
            }

            try {
                futures = es.invokeAll(callList, 100L, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            if (futures != null) {
                for (Future<Map<CacheKey, CacheValue<InMemoryItem>>> future : futures) {
                    if (future.isDone() && !future.isCancelled()) {
                        try {
                            Map<CacheKey, CacheValue<InMemoryItem>> memMap = future.get();
                            map.putAll(memMap);
                        } catch (InterruptedException | ExecutionException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
            return map;
        }

        private CacheValue<InMemoryItem> loadOne(CacheKey cacheKey) {
            String id = String.valueOf(integer.incrementAndGet());
            //            System.out.println("seq:" + id + ", key:" + cacheKey.getKey());
            try {
                TimeUnit.MICROSECONDS.sleep(1L);
            } catch (InterruptedException e) {
                // ignore
            }
            return new CacheValue<>(new InMemoryItem(id, null), 1L);
        }
    }

}
