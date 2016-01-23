package org.snapscript.compile.instruction.define;

import org.snapscript.core.CompoundScope;
import org.snapscript.core.Invocation;
import org.snapscript.core.Model;
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
      Model model = scope.getModel();
      
      if(model != null) {
         scope = new CompoundScope(model, inner, inner);
      } else {
         scope = inner;
      }
      try{
         return invocation.invoke(scope, object, list);
      }catch(Exception e){
         e.printStackTrace();
         throw e;
      }
   }
}
