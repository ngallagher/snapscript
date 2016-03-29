package org.snapscript.compile.instruction.define;

import static org.snapscript.core.Reserved.TYPE_THIS;

import org.snapscript.core.Initializer;
import org.snapscript.core.Instance;
import org.snapscript.core.Invocation;
import org.snapscript.core.Model;
import org.snapscript.core.Module;
import org.snapscript.core.ObjectInstance;
import org.snapscript.core.Scope;
import org.snapscript.core.State;
import org.snapscript.core.Type;
import org.snapscript.core.Value;
import org.snapscript.core.ValueType;

public class InstanceAllocator implements Allocator {
   
   private final Initializer initializer;
   private final Invocation invocation;
   private final boolean compile;
   
   public InstanceAllocator(Initializer initializer, Invocation invocation) {
      this(initializer, invocation, true);
   }
   
   public InstanceAllocator(Initializer initializer, Invocation invocation, boolean compile) {
      this.initializer = initializer;
      this.invocation = invocation;
      this.compile = compile;
   }
   
   @Override
   public Instance allocate(Scope scope, Instance instance, Object... list) throws Exception {
      Type real = (Type)list[0];
      Model model = scope.getModel();
      Module module = real.getModule();
      Class type = instance.getClass();
      
      if(type != ObjectInstance.class) { 
         Instance result = new ObjectInstance(module, model, instance, real);// we need to pass the base type up!!
   
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
      invocation.invoke(instance, instance, list);
      
      return instance;
   }
}