package org.snapscript.agent.profiler;

public class ProfileResult implements Comparable<ProfileResult>{
   
   private long time;
   private int line;
   
   public ProfileResult() {
      super();
   }
   
   public ProfileResult(long time, int line) {
      this.time = time;
      this.line = line;
   }
   
   @Override
   public int compareTo(ProfileResult other) {
      int compare = Long.compare(other.time, time);
      
      if(compare == 0) {
         return Integer.compare(other.line, line);
      }
      return compare;
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
