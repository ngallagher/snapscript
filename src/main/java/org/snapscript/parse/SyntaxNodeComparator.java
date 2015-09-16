package org.snapscript.parse;

import java.util.Comparator;

public class SyntaxNodeComparator implements Comparator<SyntaxNode> {
   
   private final boolean reverse;
   
   public SyntaxNodeComparator() {
      this(false);
   }
   
   public SyntaxNodeComparator(boolean reverse) {
      this.reverse = reverse;
   }

   @Override
   public int compare(SyntaxNode left, SyntaxNode right) {
      Integer leftMark = left.getStart();
      Integer rightMark = right.getStart();
      int comparison = leftMark.compareTo(rightMark);  
      
      if(comparison == 0) {
         Integer leftFinish = left.getDepth();
         Integer rightFinish = right.getDepth();
         
         return leftFinish.compareTo(rightFinish); 
      }
      return comparison;
   }

}
