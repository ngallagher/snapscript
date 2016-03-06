package org.snapscript.core.index;

import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicBoolean;

public class DefaultMethodChecker {
   
   private static final String DEFAULT_METHOD = "isDefault";

   private AtomicBoolean check;
   private Method access;
   
   public DefaultMethodChecker() {
      this.check = new AtomicBoolean(true);
   }
   
   public boolean check(Method method) throws Exception {
      if(check.compareAndSet(true, false)) {
         access = Method.class.getDeclaredMethod(DEFAULT_METHOD);
      }
      if(access != null) {
        Object result = access.invoke(method);
        Boolean value = (Boolean)result;
        
        return value.booleanValue();
      }
      return false;
    }
}
