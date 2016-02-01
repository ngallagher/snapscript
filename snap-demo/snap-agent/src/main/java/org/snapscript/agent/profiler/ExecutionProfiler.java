package org.snapscript.agent.profiler;

import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;

import org.snapscript.core.Scope;
import org.snapscript.core.Trace;
import org.snapscript.core.TraceListener;

public class ExecutionProfiler implements TraceListener {
   
   private volatile int[] counts;
   private volatile int[] visits;
   private volatile long[] start;
   private volatile long[] times;
   private volatile int max;
   
   public ExecutionProfiler() {
      this.start = new long[500];
      this.counts = new int[500];
      this.times = new long[500];
      this.visits = new int[500];
      this.max = 0;
   }

   public SortedSet<ProfileResult> lines(int size) {
      SortedSet<ProfileResult> result=new TreeSet<ProfileResult>();
      long localMax = max;
      long[] localTimes = times;
      int[] localVisits = visits;
      
      for(int i = 0; i < localMax && i < size; i++){
         if(localTimes[i] > 0) {
            long duration = TimeUnit.NANOSECONDS.toMillis(localTimes[i]);
            int visits = localVisits[i];
            result.add(new ProfileResult(duration, visits, i));
         }
      }
      return result;
   }
   
   @Override
   public void before(Scope scope, Trace trace) {
      int line = trace.getLine();
      // thread local required, also recursion counter
      if(times.length < line) {
         counts = copyOf(counts, line + 50);
         times = copyOf(times, line + 50);
         start = copyOf(start, line + 50);
         visits = copyOf(visits, line + 50);
      }
      int currentCount = counts[line]++;// we just entered an instruction
  
      if(currentCount == 0) {
         start[line] = System.nanoTime(); // first instruction to enter
      }
      visits[line]++;
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
   public void after(Scope scope, Trace trace) {
      int line = trace.getLine();
      int currentCount = --counts[line]; // exit instruction

      if(currentCount == 0) {
         times[line] += (System.nanoTime() - start[line]);
         start[line] = 0L; // reset as we are now at zero
      }
      if(line > max) {
         max=line;
      }
   }
}