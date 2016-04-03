package org.snapscript.compile.instruction.operation;

import org.snapscript.core.Context;
import org.snapscript.core.Scope;
import org.snapscript.core.convert.ProxyWrapper;

public class StringBuilder {

   public static String create(Scope scope, Object left) {
      Context context = scope.getContext();
      ProxyWrapper wrapper = context.getWrapper();
      Object object = wrapper.toProxy(left);
      
      return String.valueOf(object);
   }
   
}
