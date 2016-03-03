package org.snapscript.parse;

import java.util.Set;

import org.snapscript.common.LeastRecentlyUsedSet;

public class PositionSet {

   private final Set<Long> positions;
   
   public PositionSet(int capacity) {
      this.positions = new LeastRecentlyUsedSet<Long>(capacity);
   }
   
   public synchronized boolean add(Long position) {
      return positions.add(position);
   }
   
   public synchronized boolean contains(Long position) {
      return positions.contains(position);
   }
   
   public synchronized int size() {
      return positions.size();
   }
}
