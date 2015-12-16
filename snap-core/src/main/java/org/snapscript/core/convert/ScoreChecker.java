package org.snapscript.core.convert;

public class ScoreChecker {

   private final Class[] types;
   private final int[] scores;
   
   public ScoreChecker(Class[] types, int[] scores) {
      this.types = types;
      this.scores = scores;
   }
   
   public Integer score(Class type) {
      for(int i = 0; i < types.length; i++) {
         Class next = types[i];
         
         if(type == next) {
            return scores[i];
         }
      }
      return null;
   }
}
