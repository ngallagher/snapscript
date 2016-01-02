package org.snapscript.ide;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class SharedMemoryReader {

   public static void main(String[] args) throws FileNotFoundException, IOException, InterruptedException {
      File file = new File("c:/tmp/mapped.txt");
      FileChannel channel = new RandomAccessFile(file, "rw").getChannel();
      SharedMemoryEncoder encoder = new SharedMemoryEncoder();
      
      try {
         long capacity = 10 * 1024 * 1024;
         MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, capacity);
         long start = System.currentTimeMillis();
         int count = 0;
         
         for (int i = 0; i < 10000; i++) {
            while(true) {
               byte value = buffer.get((byte)1);
               
               if(value == 1) {
                  break;
               }
               Thread.yield();
            }
            SharedMemoryMessage message = encoder.decode(buffer);
            int product = message.getProduct();
            
            if(product != count++) {
               throw new IOException("Sequence is not correct");
            }
         }
         long finish = System.currentTimeMillis();
         long duration = finish - start;
         
         System.err.println("Time taken " + duration);
      } finally {
         channel.close();
      }
   }

}
