package org.snapscript.agent.profiler;

public class ProfileResult implements Comparable<ProfileResult>{
   
   private long time;
   private int line;
   private int count;
   
   public ProfileResult() {
      super();
   }
   
   public ProfileResult(long time, int count, int line) {
      this.time = time;
      this.line = line;
      this.count = count;
   }
   
   @Override
   public int compareTo(ProfileResult other) {
      int compare = Long.compare(other.time, time);
      
      if(compare == 0) {
         return Integer.compare(other.line, line);
      }
      return compare;
   }
   
   public int getCount() {
      return count;
   }

   public void setCount(int count) {
      this.count = count;
   }

   public int getLine(){
      return line;
   }
   
   public void setLine(Integer line) {
      this.line = line;
   }
   
   public long getTime(){
      return time;
   }
   
   public void setTime(Long time) {
      this.time = time;
   }
}
