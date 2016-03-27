package org.snapscript.compile.instruction.define;

import static org.snapscript.core.Reserved.TYPE_THIS;

import org.snapscript.core.Initializer;
import org.snapscript.core.Instance;
import org.snapscript.core.Invocation;
import org.snapscript.core.Model;
import org.snapscript.core.ObjectInstance;
import org.snapscript.core.Scope;
import org.snapscript.core.State;
import org.snapscript.core.Type;
import org.snapscript.core.Value;
import org.snapscript.core.ValueType;

public class NewInvocation implements Constructor {
   
   private final Initializer initializer;
   private final Invocation body;
   private final boolean compile;
   
   public NewInvocation(Initializer initializer, Invocation body) {
      this(initializer, body, true);
   }
   
   public NewInvocation(Initializer initializer, Invocation body, boolean compile) {
      this.initializer = initializer;
      this.compile = compile;
      this.body = body;
   }
   
   @Override
   public Instance invoke(Scope scope, Instance instance, Object... list) throws Exception {
      Type real = (Type)list[0];
      Model model = scope.getModel();
      Class type = instance.getClass();
      
      if(type != ObjectInstance.class) { 
         Instance result = new ObjectInstance(model, instance);// we need to pass the base type up!!
   
         State state = instance.getState();
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
      
      return instance;
   }
}
