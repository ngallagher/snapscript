package org.snapscript.core.index;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.snapscript.core.Function;

public class ConstructorIndexer {

   private final ConstructorGenerator generator;
   
   public ConstructorIndexer(TypeIndexer indexer) {
      this.generator = new ConstructorGenerator(indexer);
   }

   public List<Function> index(Class source) throws Exception {
      Constructor[] constructors = source.getDeclaredConstructors();
      
      if(constructors.length > 0) {
         List<Function> functions = new ArrayList<Function>();
   
         for(Constructor constructor : constructors){
            int modifiers = constructor.getModifiers();
            
            if(Modifier.isPublic(modifiers)) {
               Class[] parameters = constructor.getParameterTypes();
               Function function = generator.generate(constructor, parameters, modifiers);
               
               functions.add(function);
            }
         }
         return functions;
      }
      return Collections.emptyList();
   }
}
