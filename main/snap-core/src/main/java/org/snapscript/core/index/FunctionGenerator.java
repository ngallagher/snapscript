package org.snapscript.core.index;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.snapscript.core.Any;
import org.snapscript.core.Bug;
import org.snapscript.core.Function;
import org.snapscript.core.InternalStateException;
import org.snapscript.core.Invocation;
import org.snapscript.core.InvocationFunction;
import org.snapscript.core.Parameter;
import org.snapscript.core.ParameterBuilder;
import org.snapscript.core.Signature;
import org.snapscript.core.Type;

public class FunctionGenerator {
   
   private final DefaultMethodChecker checker;
   private final ParameterBuilder builder;
   private final TypeIndexer indexer;
   
   public FunctionGenerator(TypeIndexer indexer) {
      this.checker = new DefaultMethodChecker();
      this.builder = new ParameterBuilder();
      this.indexer = indexer;
   }

   public Function generate(Type type, Method method, Class[] types, String name, int modifiers) {
      Class real = method.getReturnType();
      boolean variable = method.isVarArgs();
      
      try {
         List<Parameter> parameters = new ArrayList<Parameter>();
   
         for(int i = 0; i < types.length; i++){
            boolean last = i + 1 == types.length;
            Type match = indexer.loadType(types[i]);
            Parameter parameter = builder.create(match, i, variable && last);
            
            parameters.add(parameter);
         }
         Signature signature = new Signature(parameters, variable);
         Invocation invocation = null;
         
         if(checker.check(method)) {
            invocation = new DefaultMethodInvocation(method);
         } else {
            invocation = new MethodInvocation(method);
         }
         Type returns = indexer.loadType(real);
         
         if(!method.isAccessible()) {
            method.setAccessible(true);
         }
         if(real != void.class && real != Any.class && real != Object.class) {
            return new InvocationFunction(signature, invocation, type, returns, name, modifiers);
         }
         return new InvocationFunction(signature, invocation, type, null, name, modifiers);
      } catch(Exception e) {
         throw new InternalStateException("Could not create function for " + method, e);
      }
   }
}
