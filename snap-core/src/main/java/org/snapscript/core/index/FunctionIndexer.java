package org.snapscript.core.index;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.snapscript.core.Function;

public class FunctionIndexer {
   
   private final FunctionGenerator generator;
   private final ConstructorIndexer indexer;
   
   public FunctionIndexer(TypeIndexer indexer){
      this.generator = new FunctionGenerator(indexer);
      this.indexer = new ConstructorIndexer(indexer);
   }

   public List<Function> index(Class source) throws Exception {
      List<Function> constructors = indexer.index(source);
      Method[] methods = source.getDeclaredMethods();
      
      if(methods.length > 0) {
         List<Function> functions = new ArrayList<Function>();
   
         for(Method method : methods){
            int modifiers = method.getModifiers();
            
            if(Modifier.isPublic(modifiers)) {
               String name = method.getName();
               Class[] parameters = method.getParameterTypes();
               Function function = generator.generate(method, parameters, name, modifiers);
               
               functions.add(function);
            }
         }
         if(!constructors.isEmpty()) {
            functions.addAll(constructors);
         }
         return functions;
      }
      return constructors;
   }
}
