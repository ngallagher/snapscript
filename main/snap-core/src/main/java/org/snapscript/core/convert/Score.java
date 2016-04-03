package org.snapscript.core.convert;

public class Score implements Comparable<Score> {
   
   public static final Score EXACT = new Score(100, true);
   public static final Score SIMILAR = new Score(70, true);
   public static final Score COMPATIBLE = new Score(20, true);
   public static final Score POSSIBLE = new Score(10, true);
   public static final Score TRANSIENT = new Score(10, false);
   public static final Score INVALID = new Score(0, true);

   private final boolean cache;
   private final Integer score;
   
   public Score(int score) {
      this(score, true);
   }
   
   public Score(int score, boolean cache) {
      this.score = score;
      this.cache = cache;
   }
   
   public boolean isFinal() {
      return cache;
   }
   
   @Override
   public int compareTo(Score other) {
      return score.compareTo(other.score);
   }
   
   @Override
   public String toString() {
      return score.toString();
   }
   
   public static Score sum(Score left, Score right) {
      return new Score(left.score + right.score, left.cache && right.cache);
   }
}
