package org.snapscript.compile.instruction.define;

import static org.snapscript.core.Reserved.TYPE_THIS;

import org.snapscript.core.Initializer;
import org.snapscript.core.Instance;
import org.snapscript.core.Invocation;
import org.snapscript.core.Model;
import org.snapscript.core.ObjectInstance;
import org.snapscript.core.Result;
import org.snapscript.core.ResultType;
import org.snapscript.core.Scope;
import org.snapscript.core.State;
import org.snapscript.core.Type;
import org.snapscript.core.Value;
import org.snapscript.core.ValueType;

public class NewInvocation implements Constructor {
   
   private final Initializer initializer;
   private final Invocation body;
   private final Scope outer;
   private final boolean compile;
   
   public NewInvocation(Scope outer, Initializer initializer, Invocation body) {
      this(outer, initializer, body, true);
   }
   
   public NewInvocation(Scope outer, Initializer initializer, Invocation body, boolean compile) {
      this.initializer = initializer;
      this.compile = compile;
      this.outer = outer;
      this.body = body;
   }
   
   @Override
   public Result invoke(Scope scope, Instance instance, Object... list) throws Exception {
      Type real = (Type)list[0];
      Model model = scope.getModel();
      Class type = instance.getClass();
      Scope primitive = instance.getScope();
      int depth = instance.getDepth();
      
      if(type != Instance.class) {
         Instance result = new ObjectInstance(model, primitive, outer, instance, real, depth + 1);// we need to pass the base type up!!
   
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
      body.invoke(instance, instance, list);
      
      return ResultType.getNormal(instance);
   }
}
