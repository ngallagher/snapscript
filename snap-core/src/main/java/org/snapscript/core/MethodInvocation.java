package org.snapscript.core;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.Arrays;

import org.snapscript.core.convert.ProxyExtractor;

public class MethodInvocation implements Invocation<Object>{

   private final ProxyExtractor extractor;
   private final Method method;
   
   public MethodInvocation(Method method) {
      this.extractor = new ProxyExtractor();
      this.method = method;
   }
   
   @Override
   public Result invoke(Scope scope, Object left, Object... list) throws Exception {
      if(method.isVarArgs()) {
         Class[] types = method.getParameterTypes();
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
                  throw new IllegalStateException("Invalid argument at " + i + " for" + method, e);
               }
            }
            list = Arrays.copyOf(list, require);            
            list[start] = array;
         }
      }
      Object value = method.invoke(left, list);
      Object result = extractor.extract(value);
      
      return new Result(ResultFlow.NORMAL, result);
   }
}
