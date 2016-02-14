package org.snapscript.core;

import java.util.Comparator;

public class TraceComparator implements Comparator<Trace> {

   private boolean reverse;
   
   public TraceComparator() {
      this(false);
   }
   
   public TraceComparator(boolean reverse) {
      this.reverse = reverse;
   }
   
   @Override
   public int compare(Trace left, Trace right) {
      String leftPath = left.getResource();
      String rightPath = right.getResource();
      int result = leftPath.compareTo(rightPath);
      
      if(result == 0) {
         Integer leftLine = left.getLine();
         Integer rightLine = right.getLine();
         
         return leftLine.compareTo(rightLine);
      }
      return result;
   }

}
