package org.snapscript.compile.instruction.define;

import org.snapscript.core.Instance;
import org.snapscript.core.Invocation;
import org.snapscript.core.Result;
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
      return constructor.invoke(scope, instance, list);
   }
}
