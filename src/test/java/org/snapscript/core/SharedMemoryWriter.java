package org.snapscript.core;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class SharedMemoryWriter {

   public static void main(String[] list) throws Exception {
      File file = new File("c:/tmp/mapped.txt");
      file.delete();

      FileChannel channel = new RandomAccessFile(file, "rw").getChannel();
      SharedMemoryEncoder encoder = new SharedMemoryEncoder();
      
      try {
         long capacity = 10 * 1024 * 1024;
         MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, capacity);
         long start = System.currentTimeMillis();
         
         for(int i = 0; i < 10000; i ++) {
            SharedMemoryMessage message = new SharedMemoryMessage(i, i, i);
            buffer.put((byte)1);
            encoder.encode(buffer, message);
            Thread.sleep(1000);
         }
         long finish = System.currentTimeMillis();
         long duration = finish - start;
         
         System.err.println("Time taken " + duration);
      } finally {
         channel.close();
      }
   }
}
