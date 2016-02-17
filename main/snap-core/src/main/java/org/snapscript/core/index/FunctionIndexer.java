package org.snapscript.core.index;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.snapscript.core.Function;
import org.snapscript.core.ModifierType;
import org.snapscript.core.Type;

public class FunctionIndexer {
   
   private final FunctionGenerator generator;
   private final ConstructorIndexer indexer;
   private final ModifierConverter converter;
   
   public FunctionIndexer(TypeIndexer indexer){
      this.generator = new FunctionGenerator(indexer);
      this.indexer = new ConstructorIndexer(indexer);
      this.converter = new ModifierConverter();
   }

   public List<Function> index(Type type) throws Exception {
      Class source = type.getType();
      List<Function> constructors = indexer.index(type);
      Method[] methods = source.getDeclaredMethods();
      
      if(methods.length > 0) {
         List<Function> functions = new ArrayList<Function>();
   
         for(Method method : methods){
            int modifiers = converter.convert(method);
            
            if(ModifierType.isPublic(modifiers)) {
               String name = method.getName();
               Class[] parameters = method.getParameterTypes();
               Function function = generator.generate(type, method, parameters, name, modifiers);
               
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
