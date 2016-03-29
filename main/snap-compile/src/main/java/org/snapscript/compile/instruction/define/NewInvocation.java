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
   public Result invoke(Scope scope, Instance object, Object... list) throws Exception {
      Instance instance = builder.create(scope, object); // merge with static inner scope
      
      if(instance != null) {
         instance.setInstance(instance); // set temporary instance
      }
      return create(scope, instance, list);
   }
   
   private Result create(Scope scope, Instance object, Object... list) throws Exception {
      Instance result = allocator.allocate(scope, object, list);
      
      if(object != null) {
         result.setInstance(result); // set instance
      }
      return ResultType.getNormal(result);
   }
}
