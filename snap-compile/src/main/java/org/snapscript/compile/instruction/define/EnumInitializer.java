package org.snapscript.compile.instruction.define;

import static org.snapscript.core.ResultFlow.NORMAL;

import java.util.List;
import java.util.concurrent.Callable;

import org.snapscript.compile.instruction.ArgumentList;
import org.snapscript.core.Constant;
import org.snapscript.core.Context;
import org.snapscript.core.Initializer;
import org.snapscript.core.Module;
import org.snapscript.core.Property;
import org.snapscript.core.Result;
import org.snapscript.core.Scope;
import org.snapscript.core.StaticAccessor;
import org.snapscript.core.Type;
import org.snapscript.core.Value;
import org.snapscript.core.bind.FunctionBinder;

public class EnumInitializer extends Initializer {
   
   private final ArgumentList list;
   private final EnumKey key;
   private final int index;
   
   public EnumInitializer(EnumKey key, ArgumentList list, int index) {
      this.index = index;
      this.list = list;
      this.key = key;
   }
   
   @Override
   public Result execute(Scope scope, Type type) throws Exception {
      Value value = key.evaluate(scope, null);
      String name = value.getString();
      
      if(type == null) {
         throw new IllegalStateException("No type found for " + name); // class not found
      }
      Callable<Result> call = bind(scope, type);
           
      if(call == null){
         throw new IllegalStateException("No constructor for " + name);
      }
      Result result = call.call();
      Scope instance = result.getValue();
      
      Initializer initializer = new CompoundInitializer();
      StaticAccessor accessor = new StaticAccessor(initializer, scope, type, name);
      Property property = new Property(name, type, accessor);
      
      // Add a static accessor to the enum type
      type.getProperties().add(property);
      Constant c=new Constant(instance, name);
      instance.getState().addConstant("name", new Constant(name, "name"));
      instance.getState().addConstant("ordinal", new Constant(index, "ordinal"));  
      scope.getState().addConstant(name, c);
      List l =scope.getState().getValue("values").getValue();
      l.add(instance);
      return NORMAL.getResult(instance);
   }
   
   private Callable<Result> bind(Scope scope, Type type) throws Exception {
      Module module = scope.getModule();
      Context context = module.getContext();
      FunctionBinder binder = context.getBinder();
      
      if(list != null) {
         Value array = list.evaluate(scope, null); // arguments have no left hand side
         Object[] arguments = array.getValue();
         Object[] expand = new Object[arguments.length + 1];
         
         for(int i = 0; i < arguments.length; i++) {
            expand[i + 1] = arguments[i];
         }
         expand[0] = type;
         
         return binder.bind(scope, type, "new", expand);
      }
      return binder.bind(scope, type, "new", type);
   }
}
