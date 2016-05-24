package org.snapscript.core.index;

import static org.snapscript.core.Reserved.TYPE_CONSTRUCTOR;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import org.snapscript.core.Bug;
import org.snapscript.core.Function;
import org.snapscript.core.InternalStateException;
import org.snapscript.core.Invocation;
import org.snapscript.core.InvocationFunction;
import org.snapscript.core.Parameter;
import org.snapscript.core.ParameterBuilder;
import org.snapscript.core.Signature;
import org.snapscript.core.Type;

public class ConstructorGenerator {

   private final ParameterBuilder builder;
   private final TypeIndexer indexer;
   
   public ConstructorGenerator(TypeIndexer indexer) {
      this.builder = new ParameterBuilder();
      this.indexer = indexer;
   }
   
   public Function generate(Type type, Constructor constructor, Class[] types, int modifiers) {
      boolean variable = constructor.isVarArgs();
      
      try {
         List<Parameter> parameters = new ArrayList<Parameter>();
   
         for(int i = 0; i < types.length; i++){
            boolean last = i + 1 == types.length;
            Type match = indexer.loadType(types[i]);
            Parameter parameter = builder.create(match, i, variable && last);
            
            parameters.add(parameter);
         }
         Signature signature = new Signature(parameters, variable);
         Invocation invocation = new ConstructorInvocation(constructor);
         
         if(!constructor.isAccessible()) {
            constructor.setAccessible(true);
         }
         return new InvocationFunction(signature, invocation, type, type, TYPE_CONSTRUCTOR, modifiers);
      } catch(Exception e) {
         throw new InternalStateException("Could not create function for " + constructor, e);
      }
   } 
}
