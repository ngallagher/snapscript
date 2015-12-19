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
            Object[] copy = new Object[require];
            
            if(require > list.length) {
               System.arraycopy(list, 0, copy, 0, list.length);
            } else {
               System.arraycopy(list, 0, copy, 0, require);
            }
            copy[start] = array;
            list = copy;
         }
      }
      Object value = method.invoke(left, list);
      Object result = extractor.extract(value);
      
      return ResultType.getNormal(result);
   }
}
