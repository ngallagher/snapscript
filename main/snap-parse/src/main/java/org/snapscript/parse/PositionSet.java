package org.snapscript.parse;

import java.util.Set;

import org.snapscript.common.LeastRecentlyUsedSet;

public class PositionSet {

   private final Set<Integer> positions;
   
   public PositionSet(int capacity) {
      this.positions = new LeastRecentlyUsedSet<Integer>(capacity);
   }
   
   public boolean add(Integer position) {
      return positions.add(position);
   }
   
   public boolean contains(Integer position) {
      return positions.contains(position);
   }
   
   public int size() {
      return positions.size();
   }
}
