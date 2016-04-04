package org.snapscript.compile.instruction.define;

import java.util.concurrent.atomic.AtomicBoolean;

import org.snapscript.core.Initializer;
import org.snapscript.core.Invocation;
import org.snapscript.core.Result;
import org.snapscript.core.ResultType;
import org.snapscript.core.Scope;
import org.snapscript.core.Type;
import org.snapscript.core.instance.Instance;

public class NewInvocation implements Invocation<Instance>{
   
   private final StaticInstanceBuilder builder;
   private final Initializer initializer;
   private final AtomicBoolean compile;
   private final Allocator allocator;
   
   public NewInvocation(Initializer initializer, Allocator allocator, Scope inner, Type type) {
      this(initializer, allocator, inner, type, true);
   }
   
   public NewInvocation(Initializer initializer, Allocator allocator, Scope inner, Type type, boolean compile) {
      this.builder = new StaticInstanceBuilder(inner, type);
      this.compile = new AtomicBoolean(compile);
      this.initializer = initializer;
      this.allocator = allocator;
   }

   @Override
   public Result invoke(Scope scope, Instance base, Object... list) throws Exception {
      Type real = (Type)list[0];
      Instance instance = builder.create(scope, base, real); // merge with static inner scope
      
      if(initializer != null) {
         if(compile.compareAndSet(true, false)) {
            initializer.compile(scope, real); // static stuff if needed
         }
      }
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
