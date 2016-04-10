package org.snapscript.core.index;

import static org.snapscript.core.Reserved.TYPE_CONSTRUCTOR;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import org.snapscript.core.Function;
import org.snapscript.core.InternalStateException;
import org.snapscript.core.Invocation;
import org.snapscript.core.InvocationFunction;
import org.snapscript.core.Signature;
import org.snapscript.core.Type;

public class ConstructorGenerator {
   
   private static final String[] PREFIX = {
   "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", 
   "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z" };

   private final TypeIndexer indexer;
   
   public ConstructorGenerator(TypeIndexer indexer) {
      this.indexer = indexer;
   }
   
   public Function generate(Type type, Constructor constructor, Class[] parameters, int modifiers) {
      boolean variable = constructor.isVarArgs();
      
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
