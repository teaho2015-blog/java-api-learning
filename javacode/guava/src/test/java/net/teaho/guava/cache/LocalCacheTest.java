package net.teaho.guava.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Lists;

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

    private static final int CACHE_MAXIMUM_SIZE = 10_000;
    private static final int CACHE_INITIAL_SIZE = 1_000;
    private static final CacheValue<InMemoryItem> emptyCacheValue = new CacheValue<>(null, 0);

    @Test
    public void testCacheSimpleUsage() throws ExecutionException {

        Cache<CacheKey, CacheValue<InMemoryItem>> cache = CacheBuilder
            .newBuilder()
            .maximumSize(10)
            .expireAfterWrite(60, TimeUnit.SECONDS)
            .build();

        CacheKey cacheKey = new CacheKey("key", "field");
        CacheValue<InMemoryItem> item = cache.get(cacheKey, () -> LoadingCacheLoader.loadOne(cacheKey));
        System.out.println("item:" + item);

        CacheKey cacheKey2 = new CacheKey("key2", "field2");
        CacheValue<InMemoryItem> item2 = cache.getIfPresent(cacheKey2);
        System.out.println("item:" + item2);

        System.out.println("items" + cache.getAllPresent(Lists.newArrayList(cacheKey, cacheKey2)));

        cache.invalidate(cacheKey);

    }

    @Test
    public void testLoadingCacheSimpleUsage() throws ExecutionException {

        LoadingCache<CacheKey, CacheValue<InMemoryItem>> cache = CacheBuilder
            .newBuilder()
            .maximumSize(10000)
            .expireAfterWrite(60, TimeUnit.SECONDS)
            .build(new LoadingCacheLoader());


        CacheKey cacheKey = new CacheKey("key", "field");
        CacheValue<InMemoryItem> item = cache.get(cacheKey, () -> LoadingCacheLoader.loadOne(cacheKey));
        System.out.println("item:" + item);

        CacheKey cacheKey2 = new CacheKey("key2", "field2");
        CacheValue<InMemoryItem> item2 = cache.get(cacheKey2);
        System.out.println("item:" + item2);

        System.out.println("items" + cache.getAll(Lists.newArrayList(cacheKey, cacheKey2)));

        cache.invalidate(cacheKey);

    }

    @Test
    public void testCacheGet() {
        Cache<CacheKey, CacheValue<InMemoryItem>> cache = CacheBuilder
            .newBuilder()
            //是否记录localcache状态（命中率、缓存个数、失败个数、异常数），默认开启
            .recordStats()
            //最大缓存容量
            .maximumSize(CACHE_MAXIMUM_SIZE)
            //初始容量
            .initialCapacity(CACHE_INITIAL_SIZE)
            //缓存item被剔除时的监听器，能获取到key、value、剔除原因（手动剔除、过期、gc回收、超过最大item数驱逐）
            .removalListener(notification -> {
                System.out.println("key:" + notification.getKey() + ",val:" + notification.getValue() + ", cause" + notification.getCause());
            })
            //key写入后的过期时间
            .expireAfterWrite(30 * 60, TimeUnit.SECONDS)
            //
            .concurrencyLevel(100)
            //设置key为weak key
            .weakKeys()
            //设置value为soft引用
            .softValues()
            .build();


        CacheKey cacheKey = new CacheKey("key", "field");
        try {
            CacheValue<InMemoryItem> item = cache.get(cacheKey, new Callable<CacheValue<InMemoryItem>>() {
                @Override
                public CacheValue<InMemoryItem> call() throws Exception {
                    return LoadingCacheLoader.loadOne(cacheKey);
                }
            });
            System.out.println(item.getV().id);
        } catch (ExecutionException e) {
            System.out.println("exception" + e.getMessage());
        }

    }


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
                    .collect(Collectors.toMap(cacheKey -> cacheKey, LoadingCacheLoader::loadOne));
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

        private static CacheValue<InMemoryItem> loadOne(CacheKey cacheKey) {
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
