package org.snapscript.core.bind;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.snapscript.core.Type;

public class TypePathBuilder {
   
   private final Map<Type, List<Type>> paths;
   
   public TypePathBuilder() {
      this.paths = new HashMap<Type, List<Type>>();
   }

   public List<Type> createPath(Type type, String name) {
      if(name.equals("new")) {
         return Arrays.asList(type);
      }
      List<Type> path = paths.get(type);
      Class real = type.getType();
      
      if(path == null) {
         List<Type> result = new ArrayList<Type>();
      
         collectClasses(type, result);
      
         if(real == null) {
            collectTraits(type, result);
         }
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
