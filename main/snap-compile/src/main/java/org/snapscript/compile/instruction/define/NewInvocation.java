package org.snapscript.compile.instruction.define;

import org.snapscript.core.Instance;
import org.snapscript.core.Invocation;
import org.snapscript.core.Result;
import org.snapscript.core.ResultType;
import org.snapscript.core.Scope;
import org.snapscript.core.Type;

public class NewInvocation implements Invocation<Instance>{
   
   private final StaticInstanceBuilder builder;
   private final Allocator allocator;
   
   public NewInvocation(Allocator allocator, Scope inner, Type type) {
      this.builder = new StaticInstanceBuilder(inner, type);
      this.allocator = allocator;
   }

   @Override
   public Result invoke(Scope scope, Instance base, Object... list) throws Exception {
      Type real = (Type)list[0];
      Instance instance = builder.create(scope, base, real); // merge with static inner scope
      
      if(instance != null) {
         instance.setInstance(instance); // set temporary instance
      }
      return create(scope, instance, list);
   }
   
   private Result create(Scope scope, Instance base, Object... list) throws Exception {
      Instance result = allocator.allocate(scope, base, list);
      
      if(result != null) {
         result.setInstance(result); // set instance
      }
      return ResultType.getNormal(result);
   }
}
