package org.snapscript.core.index;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.snapscript.core.Function;

public class ConstructorIndexer {

   private final ConstructorGenerator generator;
   private final ModifierConverter converter;
   
   public ConstructorIndexer(TypeIndexer indexer) {
      this.generator = new ConstructorGenerator(indexer);
      this.converter = new ModifierConverter();
   }

   public List<Function> index(Class source) throws Exception {
      Constructor[] constructors = source.getDeclaredConstructors();
      
      if(constructors.length > 0) {
         List<Function> functions = new ArrayList<Function>();
   
         for(Constructor constructor : constructors){
            int modifiers = converter.convert(constructor); // accept all consructors public/private
            Class[] parameters = constructor.getParameterTypes();
            Function function = generator.generate(constructor, parameters, modifiers);
            
            functions.add(function);
         }
         return functions;
      }
      return Collections.emptyList();
   }
}
