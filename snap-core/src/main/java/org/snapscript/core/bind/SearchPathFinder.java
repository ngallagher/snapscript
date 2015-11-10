package org.snapscript.core.bind;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.snapscript.core.Type;

// This MUST have a unit test to ensure correct path resolution
public class SearchPathFinder {
   
   private final Map<Type, List<Type>> paths;
   
   public SearchPathFinder() {
      this.paths = new HashMap<Type, List<Type>>();
   }

   public List<Type> createPath(Type type) {
      List<Type> path = paths.get(type);
      
      if(path == null) {
         List<Type> result = new ArrayList<Type>();
      
         collectClasses(type, result);
         collectTraits(type, result);

         paths.put(type, result);
         return result;
      }
      return path;
   }
   
   private void collectTraits(Type type, List<Type> done) {
      List<Type> types = type.getTypes();
      Iterator<Type> iterator = types.iterator();
      
      if(iterator.hasNext()) {
         Type next = iterator.next(); // next in line, i.e base
         
         for(Type entry : types) {
            if(!done.contains(entry)) {
               done.add(entry);
            }
         }
         collectTraits(next, done);
      }
   }
   
   // we need to check if its a class or a trait here!!!
   private void collectClasses(Type type, List<Type> done) {
      List<Type> types = type.getTypes();
      Iterator<Type> iterator = types.iterator();
      
      done.add(type);
      
      if(iterator.hasNext()) {
         Type next = iterator.next();
         
         if(!done.contains(next)) {
            collectClasses(next, done);
         }
      }
   }
}
