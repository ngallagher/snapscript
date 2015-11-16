package org.snapscript.compile.instruction.define;

import org.snapscript.core.Invocation;
import org.snapscript.core.Result;
import org.snapscript.core.Scope;

public class TypeInvocation implements Invocation<Object>{
   
   private final Invocation<Object> invocation;
   private final Scope inner;
   
   public TypeInvocation(Invocation<Object> invocation, Scope inner) {
      this.invocation = invocation;
      this.inner = inner;
   }

   @Override
   public Result invoke(Scope scope, Object object, Object... list) throws Exception {
      return invocation.invoke(inner, object, list);
   }

}
