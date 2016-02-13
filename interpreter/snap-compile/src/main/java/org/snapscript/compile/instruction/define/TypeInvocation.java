package org.snapscript.compile.instruction.define;

import org.snapscript.core.Invocation;
import org.snapscript.core.Result;
import org.snapscript.core.Scope;

public class TypeInvocation implements Invocation<Object>{
   
   private final StaticScopeMerger merger;
   private final Invocation invocation;
   private final Scope inner;
   
   public TypeInvocation(Invocation invocation, Scope inner) {
      this.merger = new StaticScopeMerger(inner);
      this.invocation = invocation;
      this.inner = inner;
   }

   @Override
   public Result invoke(Scope scope, Object object, Object... list) throws Exception {
      Scope result = merger.merge(scope);
      return invocation.invoke(result, object, list);
   }
}
