package org.snapscript.core.bind;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.snapscript.core.Type;

// This MUST have a unit test to ensure correct path resolution
public class FunctionPathFinder {
   
   private final Map<Type, Set<Type>> paths;
   
   public FunctionPathFinder() {
      this.paths = new HashMap<Type, Set<Type>>();
   }

   public Set<Type> createPath(Type type) {
      Set<Type> path = paths.get(type);
      
      if(path == null) {
         Set<Type> classes = new LinkedHashSet<Type>();
         Set<Type> traits = new LinkedHashSet<Type>();
      
         collectClasses(type, classes);
         collectTraits(type, traits);
      
         if(!traits.isEmpty()) {
            classes.addAll(traits);
         }
         paths.put(type, classes);
         return classes;
      }
      return path;
   }
   
   private void collectTraits(Type type, Set<Type> done) {
      List<Type> types = type.getTypes();
      Iterator<Type> iterator = types.iterator();
      
      if(iterator.hasNext()) {
         Type next = iterator.next(); // next in line, i.e base
         
         for(Type entry : types) {
            done.add(entry);
         }
         if(!done.contains(next)) {
            collectTraits(next, done);
         }
      }
   }
   
   // we need to check if its a class or a trait here!!!
   private void collectClasses(Type type, Set<Type> done) {
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
