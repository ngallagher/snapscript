package org.snapscript.compile.instruction.define;

import static org.snapscript.core.Reserved.TYPE_CLASS;
import static org.snapscript.core.Reserved.TYPE_THIS;

import org.snapscript.core.Bug;
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
   private final boolean enumeration;
   
   public ConstructInvocation(Scope outer, Initializer initializer, Invocation constructor) {
      this(outer, initializer, constructor, false);
   }
   
   @Bug("Find better way to pass enum bool")
   public ConstructInvocation(Scope outer, Initializer initializer, Invocation constructor, boolean enumeration) {
      this.constructor = constructor;
      this.initializer = initializer;
      this.enumeration = enumeration;
      this.outer = outer;
   }
   
   @Override
   public Result invoke(Scope scope, Scope instance, Object... list) throws Exception {
      Type real = (Type)list[0];
      Model model = scope.getModel();
      Class type = instance.getClass();
      
      if(type != InstanceScope.class) {
         InstanceScope wrapper = new InstanceScope(model, outer, instance, real);// we need to pass the base type up!!
   
         State state = wrapper.getState();
         Value self = ValueType.getConstant(wrapper, real);
         Value info = ValueType.getConstant(real); 
   
         state.addConstant(TYPE_CLASS, info);    
         state.addConstant(TYPE_THIS, self);
         instance = wrapper;
         
         if(initializer != null) {
            //body.compile(scope, real); // static stuff if needed
            // This should be done only for non-enums!!
            if(!enumeration) {
               initializer.compile(scope, real); // static stuff if needed
            }
            initializer.execute(instance, real);
         }
      }
      constructor.invoke(instance, instance, list);
      
      return ResultType.getNormal(instance);
   }
}
