package org.snapscript.core.index;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.snapscript.core.Function;
import org.snapscript.core.ModifierType;
import org.snapscript.core.Type;
import org.snapscript.core.extend.ClassExtender;

public class FunctionIndexer {
   
   private final FunctionGenerator generator;
   private final ConstructorIndexer indexer;
   private final ModifierConverter converter;
   private final ClassExtender extender;
   
   public FunctionIndexer(TypeIndexer indexer, ClassExtender extender){
      this.generator = new FunctionGenerator(indexer);
      this.indexer = new ConstructorIndexer(indexer);
      this.converter = new ModifierConverter();
      this.extender = extender;
   }

   public List<Function> index(Type type) throws Exception {
      Class source = type.getType();
      List<Function> extensions = extender.extend(source);
      List<Function> constructors = indexer.index(type);
      Method[] methods = source.getDeclaredMethods();
      
      for(Function extension : extensions) {
         constructors.add(extension);
      }
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
