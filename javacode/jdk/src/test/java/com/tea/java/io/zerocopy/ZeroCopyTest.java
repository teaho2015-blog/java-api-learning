package com.tea.java.io.zerocopy;

import org.junit.Test;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

/**
 * @author teaho2015@gmail.com
 * @date 2025-04
 */
public class ZeroCopyTest {

    @Test
    public void testNormalIO() {
        String sourceFile = "/home/teaho/IdeaProjects/bookspace/java-api-learning/javacode/jdk/src/test/resources/test.json";
        String targetFile = "/home/teaho/IdeaProjects/bookspace/java-api-learning/javacode/jdk/src/test/resources/test_copy.json";

        try (FileReader reader = new FileReader(sourceFile);
            FileWriter writer = new FileWriter(targetFile)) {

            char[] chars = new char[1024];
            int count;
            while ((count = reader.read(chars)) != -1) {
                writer.write(chars, 0, count);
            }

            System.out.println("read write file done！");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @Test
    public void sendFile() {
        String sourceFile = "/home/teaho/IdeaProjects/bookspace/java-api-learning/javacode/jdk/src/test/resources/test.json";
        String targetFile = "/home/teaho/IdeaProjects/bookspace/java-api-learning/javacode/jdk/src/test/resources/test_copy.json";
        try (FileInputStream fis = new FileInputStream(sourceFile);
            FileOutputStream fos = new FileOutputStream(targetFile);
            FileChannel inChannel = fis.getChannel();
            FileChannel outChannel = fos.getChannel()) {

            // 零拷贝传输
            inChannel.transferTo(0, inChannel.size(), outChannel);

            // 或者使用transferFrom
            // outChannel.transferFrom(inChannel, 0, inChannel.size());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    @Test
    public void mmap() throws FileNotFoundException {

        String sourceFile = "/home/teaho/IdeaProjects/bookspace/java-api-learning/javacode/jdk/src/test/resources/test.json";
        try (FileChannel fileChannel = new RandomAccessFile(new File(sourceFile), "rw").getChannel();) {
            MappedByteBuffer mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, 0, fileChannel.size());

            byte[] data = "test".getBytes(StandardCharsets.UTF_8);
            int position = 0;
            ByteBuffer subBuffer = mappedByteBuffer.slice();
            subBuffer.position(position);
            subBuffer.put(data);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
