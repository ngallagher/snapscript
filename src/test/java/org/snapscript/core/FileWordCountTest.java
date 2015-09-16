package org.snapscript.core;

import java.io.File;
import java.io.FileInputStream;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import junit.framework.TestCase;

public class FileWordCountTest extends TestCase {
   
   public static class WordCount implements Comparable<WordCount> {
      private final String word;
      private int count;
      public WordCount(String word, int count){
         this.word = word;
         this.count = count;
      }
      public int compareTo(WordCount count) {
         return Integer.compare(this.count, count.count);
      }
   }
   private static class WordScanner {
      
      private StringBuilder builder;
      private char[] buffer;
      private int count;
      private int off;
      
      public WordScanner() {
         this.builder = new StringBuilder();
         this.buffer = new char[10 * 8192];
      }
      
      public String next(CharBuffer decoder) {
         int size = 0;
         
         while(true) {
            if(off >= count) {
               int remaining = decoder.remaining();
               
               if(remaining == 0) {
                  if(size > 0) {
                     String string = builder.toString();
                     builder.setLength(0);
                     return string;
                  }
                  return null;
               }else {
                  if(remaining < buffer.length) {
                     decoder.get(buffer, 0, remaining);
                     count = remaining;
                  } else {
                     decoder.get(buffer);
                     count = buffer.length;
                  }
                  off = 0;
               }
            }
            char c = buffer[off++];
            
            if(c == ' ' || c== '\t' || c=='\n' || c=='\r') {
               if(size > 0) {
                  String string = builder.toString();
                  builder.setLength(0);
                  return string;
               }
            } else {
               builder.append(c);
               size++;
            }
         }
      }
   }
   
   
   
   public void testWordCount() throws Exception {
/*      DecimalFormat format = new DecimalFormat("###,###,###,###.###");
      File file = new File("c:\\work\\temp\\big2.txt");
      FileChannel channel = new FileInputStream(file).getChannel();
      long size = file.length();
      long start = System.currentTimeMillis();
      MappedByteBuffer buffer = channel.map(MapMode.READ_ONLY, 0, size);
      buffer.load();
      long finish = System.currentTimeMillis();
      long duration = finish - start;
      System.err.println("load="+duration);
      
      start = System.currentTimeMillis();
      Charset charset = Charset.forName("UTF-8");
      CharBuffer decoder = charset.decode(buffer);
      finish = System.currentTimeMillis();
      duration = finish - start;
      System.err.println("decode="+duration);
      int total = 0;
      

      Map<String, WordCount> counts = new HashMap<String, WordCount>();
      WordScanner scanner = new WordScanner();
      List<WordCount> list = new ArrayList<WordCount>(); 
      start = System.currentTimeMillis();
      
      while(true){
         String token = scanner.next(decoder);
         if(token == null) {
            break;
         }
         total++;
         WordCount count = counts.get(token);
         if(count==null) {
            count = new WordCount(token, 1);
            counts.put(token, count);
            list.add(count);
         } else {
            count.count++;
         }
      }
      finish = System.currentTimeMillis();
      duration = finish - start;
      
      System.out.println("parse="+duration+" total="+format.format(total));
      
      long start1 = System.currentTimeMillis();
      Collections.sort(list);
      for(int i = 0; i < 10; i++) {
         list.get(i);
      }
      long finish1 = System.currentTimeMillis();
      long duration1 = finish1 - start1;
      
      long start2 = System.currentTimeMillis();
      PriorityQueue queue = new PriorityQueue();
      queue.addAll(list);
      
      for(int i = 0; i < 10; i++) {
         queue.poll();
      }
      long finish2 = System.currentTimeMillis();
      long duration2 = finish2 - start2;
    
      System.err.println("time1="+duration1+" time2="+duration2);*/
   }

}
