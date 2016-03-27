package org.snapscript.compile.instruction.define;

import org.snapscript.core.Instance;
import org.snapscript.core.Invocation;
import org.snapscript.core.Result;
import org.snapscript.core.ResultType;
import org.snapscript.core.Scope;
import org.snapscript.core.Type;

public class NewStaticInvocation implements Invocation<Instance>{
   
   private final StaticInstanceBuilder builder;
   private final Constructor constructor;
   
   public NewStaticInvocation(Constructor constructor, Scope inner, Type type) {
      this.builder = new StaticInstanceBuilder(inner, type);
      this.constructor = constructor;
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
      Instance result = constructor.invoke(scope, object, list);
      
      if(object != null) {
         result.setInstance(result); // set instance
      }
      return ResultType.getNormal(result);
   }
}
