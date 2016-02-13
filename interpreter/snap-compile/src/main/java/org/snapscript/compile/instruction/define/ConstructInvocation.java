package org.snapscript.compile.instruction.define;

import static org.snapscript.core.Reserved.TYPE_THIS;

import org.snapscript.core.Initializer;
import org.snapscript.core.InstanceScope;
import org.snapscript.core.Invocation;
import org.snapscript.core.Model;
import org.snapscript.core.Result;
import org.snapscript.core.ResultType;
import org.snapscript.core.Scope;
import org.snapscript.core.State;
import org.snapscript.core.Type;
import org.snapscript.core.Value;
import org.snapscript.core.ValueType;

public class ConstructInvocation implements Invocation<Scope> {
   
   private final Initializer initializer;
   private final Invocation constructor;
   private final Scope outer;
   private final boolean compile;
   
   public ConstructInvocation(Scope outer, Initializer initializer, Invocation constructor) {
      this(outer, initializer, constructor, true);
   }
   
   public ConstructInvocation(Scope outer, Initializer initializer, Invocation constructor, boolean compile) {
      this.constructor = constructor;
      this.initializer = initializer;
      this.compile = compile;
      this.outer = outer;
   }
   
   @Override
   public Result invoke(Scope scope, Scope instance, Object... list) throws Exception {
      Type real = (Type)list[0];
      Model model = scope.getModel();
      Class type = instance.getClass();
      
      if(type != InstanceScope.class) {
         InstanceScope result = new InstanceScope(model, outer, instance, real);// we need to pass the base type up!!
   
         State state = result.getState();
         Value constant = ValueType.getConstant(result, real);
    
         state.addConstant(TYPE_THIS, constant); // reference to 'this'
         instance = result;
         
         if(initializer != null) {
            if(compile) {
               initializer.compile(scope, real); // static stuff if needed
            }
            initializer.execute(instance, real);
         }
      }
      constructor.invoke(instance, instance, list);
      
      return ResultType.getNormal(instance);
   }
}
