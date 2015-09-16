package org.snapscript.core;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.util.Arrays;

import org.snapscript.core.execute.Result;
import org.snapscript.core.execute.ResultFlow;

public class ConstructorInvocation implements Invocation<Object> {

   private final Constructor constructor;
   
   public ConstructorInvocation(Constructor constructor) {
      this.constructor = constructor;
   }
   
   @Override
   public Result invoke(Scope scope, Object left, Object... list) throws Exception {
      if(constructor.isVarArgs()) {
         Class[] types = constructor.getParameterTypes();
         int require = types.length;
         int actual = list.length;
         int start = require - 1;
         int remaining = actual - start;

         if(remaining >= 0) {
            Class type = types[require - 1];
            Class component = type.getComponentType();
            Object array = Array.newInstance(component, remaining);
            
            for(int i = 0; i < remaining; i++) {
               try {
                  Array.set(array, i, list[i + start]);
               } catch(Exception e){
                  throw new IllegalStateException("Invalid argument at " + i + " for" + constructor, e);
               }
            }
            list = Arrays.copyOf(list, require);                        
            list[start] = array;
         }
      }     
      Object value = constructor.newInstance(list);
      return new Result(ResultFlow.NORMAL, value);
   }
}
