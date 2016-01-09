package org.snapscript.web;

import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;

import org.snapscript.core.Scope;
import org.snapscript.core.TraceInterceptor;

public class ScriptProfiler implements TraceInterceptor {
   
   private int[] counts;
   private long[] start;
   private long[] times;
   private int max;
   
   public ScriptProfiler() {
      this.start = new long[500];
      this.counts = new int[500];
      this.times = new long[500];
      this.max = 0;
   }

   public SortedSet<ProfileResult> lines() {
      SortedSet<ProfileResult> result=new TreeSet<ProfileResult>();
    
      for(int i = 0; i < max; i++){
         if(times[i] > 0) {
            result.add(new ProfileResult(times[i], i));
         }
      }
      return result;
   }
   
   @Override
   public void before(Scope scope, Object instruction, String resource, int line, int key) {
      // thread local required, also recursion counter
      if(times.length < line) {
         counts = copyOf(counts, line + 50);
         times = copyOf(times, line + 50);
         start = copyOf(start, line + 50);
      }
      int currentCount = counts[line]++;// we just entered an instruction
      
      if(currentCount == 0) {
         start[line] = System.nanoTime(); // first instruction to enter
      }
   }
   
   private int[] copyOf(int[] array, int newSize) {
      int[] copy = new int[newSize];
      System.arraycopy(array, 0, copy, 0, Math.min(newSize, array.length));
      return copy;
   }
   
   private long[] copyOf(long[] array, int newSize) {
      long[] copy = new long[newSize];
      System.arraycopy(array, 0, copy, 0, Math.min(newSize, array.length));
      return copy;
   }

   @Override
   public void after(Scope scope, Object instruction, String resource, int line, int key) {
      int currentCount = --counts[line]; // exit instruction
      
      if(currentCount == 0) {
         times[line] += (System.nanoTime() - start[line]);
         start[line] = 0L; // reset as we are now at zero
      }
      if(line > max) {
         max=line;
      }
   }
   public static class ProfileResult implements Comparable<ProfileResult>{
      private final Long time;
      private final Integer line;
      
      private ProfileResult(Long time, Integer line) {
         this.time = time;
         this.line = line;
      }
      @Override
      public int compareTo(ProfileResult e) {
         int compare = e.time.compareTo(time);
         if(compare == 0) {
            return e.line.compareTo(line);
         }
         return compare;
      }
      public int getLine(){
         return line;
      }
      public long getTime(){
         return TimeUnit.NANOSECONDS.toMillis(time);
      }
   }
   
}