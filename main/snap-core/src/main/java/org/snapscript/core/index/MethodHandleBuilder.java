package org.snapscript.core.index;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class MethodHandleBuilder {

   private static final String ALLOWED_MODES = "allowedModes";
   
   private final Method method;

   public MethodHandleBuilder(Method method) {
      this.method = method;
   }
   
   public MethodHandle create() throws Exception {
      Class target = method.getDeclaringClass();
      Lookup lookup = MethodHandles.lookup();
      Lookup actual = lookup.in(target);
      Class access = lookup.getClass();
      Field allowedModes = access.getDeclaredField(ALLOWED_MODES);
      
      allowedModes.setAccessible(true);
      allowedModes.set(actual, Modifier.PRIVATE);
      
      return actual.unreflectSpecial(method, target);
   }
}
