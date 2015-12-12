package org.snapscript.interpret.console;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class HeatMap {

   private final Map<Integer, Integer> heat;
   private final AtomicInteger hottest;
   private final int scale;
   
   public HeatMap() {
      this(20);
   }
   
   public HeatMap(int scale) {
      this.heat = new ConcurrentHashMap<Integer, Integer>();
      this.hottest = new AtomicInteger();
      this.scale = scale;
   }
   
   public void setHeat(int line, int value) {
      int hot = hottest.get();
      
      if(value > hot) {
         hottest.set(value);
      }
      heat.put(line, value);
   }
   
   public int getHeat(int line) {
      Integer value = heat.get(line);
      
      if(value == null) {
         return 0;
      }
      float hot = hottest.get();
      float fraction = value / hot;
     
      return Math.round(fraction * scale);
   }
   
   public void clear(){
      heat.clear();
      hottest.set(0);
   }
}
