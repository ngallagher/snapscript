package org.snapscript.core.index;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.snapscript.core.Any;
import org.snapscript.core.Function;
import org.snapscript.core.InternalStateException;
import org.snapscript.core.Invocation;
import org.snapscript.core.InvocationFunction;
import org.snapscript.core.Signature;
import org.snapscript.core.Type;

public class FunctionGenerator {
   
   private static final String[] PREFIX = {
   "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", 
   "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z" };

   private final DefaultMethodChecker checker;
   private final TypeIndexer indexer;
   
   public FunctionGenerator(TypeIndexer indexer) {
      this.checker = new DefaultMethodChecker();
      this.indexer = indexer;
   }
   
   public Function generate(Type type, Method method, Class[] parameters, String name, int modifiers) {
      Class real = method.getReturnType();
      boolean variable = method.isVarArgs();
      
      try {
         List<Type> types = new ArrayList<Type>();
         List<String> names = new ArrayList<String>();
   
         for(int i = 0; i < parameters.length; i++){
            Type parameter = indexer.loadType(parameters[i]);
            String prefix = PREFIX[i % PREFIX.length];
            
            if(i > PREFIX.length) {
               prefix += i / PREFIX.length;
            }
            types.add(parameter);
            names.add(prefix);
         }
         Signature signature = new Signature(names, types, variable);
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
