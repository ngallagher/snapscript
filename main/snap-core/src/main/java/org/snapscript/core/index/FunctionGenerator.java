package org.snapscript.core.index;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.snapscript.core.Function;
import org.snapscript.core.InternalStateException;
import org.snapscript.core.Invocation;
import org.snapscript.core.Signature;
import org.snapscript.core.Type;

public class FunctionGenerator {

   private final TypeIndexer indexer;
   
   public FunctionGenerator(TypeIndexer indexer) {
      this.indexer = indexer;
   }
   
   public Function generate(Type type, Method method, Class[] parameters, String name, int modifiers) {
      boolean variable = method.isVarArgs();
      
      try {
         List<Type> types = new ArrayList<Type>();
         List<String> names = new ArrayList<String>();
   
         for(int i = 0; i < parameters.length; i++){
            Type parameter = indexer.loadType(parameters[i]);
   
            types.add(parameter);
            names.add("a" + i);
         }
         Signature signature = new Signature(names, types, variable);
         Invocation invocation = new MethodInvocation(method);
         
         if(!method.isAccessible()) {
            method.setAccessible(true);
         }
         return new Function(signature, invocation, type, name, modifiers);
      } catch(Exception e) {
         throw new InternalStateException("Could not create function for " + method, e);
      }
   }
}
