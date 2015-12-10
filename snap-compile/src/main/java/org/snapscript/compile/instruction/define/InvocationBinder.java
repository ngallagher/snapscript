package org.snapscript.compile.instruction.define;

import java.util.List;
import java.util.concurrent.Callable;

import org.snapscript.compile.instruction.collection.ArrayConverter;
import org.snapscript.core.Context;
import org.snapscript.core.Transient;
import org.snapscript.core.Module;
import org.snapscript.core.Result;
import org.snapscript.core.ResultFlow;
import org.snapscript.core.Scope;
import org.snapscript.core.Type;
import org.snapscript.core.Value;
import org.snapscript.core.bind.FunctionBinder;

public class InvocationBinder {

   /*
    * These are the only types to worry about!!!!!
    * 
    * 1) Scope of a type [if type then use the object]
    *       a) What if in a member function, then where do I get instance scope
    *       
    * 2) Scope of local [if local the the module scope is what to use]
    * 3) Scope of a module [if module use the module scope]
    * 
    */
   public InvocationDispatcher dispatch(Scope scope, Object left) {
      if(left != null) {
         Class type = left.getClass();
         
         if(Scope.class.isInstance(left)) {
            return new ScopeHandler(scope, left);            
         }
         if(Module.class.isInstance(left)) {
            return new ModuleHandler(scope, left);
         }  
         if(Type.class.isInstance(left)) {
            return new TypeHandler(scope, left);
         }  
         if(type.isArray()) {
            return new ArrayHandler(scope, left);
         }
         return new ObjectHandler(scope, left);
      }
      Type type = scope.getType();
      
      if(type != null) {
         return new ScopeHandler(scope, scope);
      }
      return new LocalHandler(scope);      
   }
   
   private class TypeHandler implements InvocationDispatcher {
      
      private final Scope scope;      
      private final Type type;
      
      public TypeHandler(Scope scope, Object object) {
         this.type = (Type)object;
         this.scope = scope;
      }

      @Override
      public Value dispatch(String name, Object... arguments) throws Exception {
         Module module = scope.getModule();
         Context context = module.getContext();
         FunctionBinder binder = context.getBinder();    
         Callable<Result> call = binder.bind(scope, type, name, arguments);
         
         if(call == null) {
            throw new IllegalStateException("Method '" + name + "' not found for type '" + type + "'");
         }
         Result result = call.call();
         ResultFlow flow = result.getFlow();
         Object data = result.getValue();
         
         if(flow == ResultFlow.THROW) {
            throw new IllegalStateException("Method '" + name + "' for type '" + type + "' had an exception");
         }
         return new Transient(data);           
      } 
   } 
   
   private class ModuleHandler implements InvocationDispatcher {
      
      private final Module module;
      private final Scope scope;      
      
      public ModuleHandler(Scope scope, Object object) {
         this.module = (Module)object;
         this.scope = scope;
      }

      @Override
      public Value dispatch(String name, Object... arguments) throws Exception {
         Scope scope = module.getScope();
         Context context = module.getContext();
         FunctionBinder binder = context.getBinder();    
         Callable<Result> call = binder.bind(scope, module, name, arguments);
         
         if(call == null) {
            throw new IllegalStateException("Method '" + name + "' not found in module '" + module + "'");
         }
         Result result = call.call();
         ResultFlow flow = result.getFlow();
         Object data = result.getValue();
         
         if(flow == ResultFlow.THROW) {
            throw new IllegalStateException("Method '" + name + "' for module '" + module + "' had an exception");
         }
         return new Transient(data);           
      }
   }     
   
   private class ScopeHandler implements InvocationDispatcher {
      
      private final Scope object;
      private final Scope scope;      
      
      public ScopeHandler(Scope scope, Object object) {
         this.object = (Scope)object;
         this.scope = scope;
      }

      @Override
      public Value dispatch(String name, Object... arguments) throws Exception {
         Module module = scope.getModule();
         Context context = module.getContext();
         FunctionBinder binder = context.getBinder();
         Callable<Result> local = binder.bind(scope, object, name, arguments);
         
         if(local == null) {
            Callable<Result> external = binder.bind(scope, module, name, arguments);
            
            if(external != null) {
               Result result = external.call();
               ResultFlow flow = result.getFlow();
               Object data = result.getValue();
               
               if(flow == ResultFlow.THROW) {
                  throw new IllegalStateException("Method '" + name + "' for module '" + module + "' had an exception");
               }
               return new Transient(data);   
            }
         }
         Type type = object.getType();
         
         if(local == null) {
            throw new IllegalStateException("Method '" + name + "' not found for type '" + type + "'");
         }
         Result result = local.call();
         ResultFlow flow = result.getFlow();
         Object data = result.getValue();
         
         if(flow == ResultFlow.THROW) {
            throw new IllegalStateException("Method '" + name + "' for type '" + type + "' had an exception");
         }
         return new Transient(data);           
      }
   }  
   
   private class ArrayHandler implements InvocationDispatcher {
      
      private final ArrayConverter converter;
      private final Object object;
      private final Scope scope;      
      
      public ArrayHandler(Scope scope, Object object) {
         this.converter = new ArrayConverter();
         this.object = object;
         this.scope = scope;
      }

      @Override
      public Value dispatch(String name, Object... arguments) throws Exception {
         Module module = scope.getModule();
         Context context = module.getContext();
         FunctionBinder binder = context.getBinder();
         List list = converter.convert(object);
         Callable<Result> call = binder.bind(scope, list, name, arguments);
         Class type = object.getClass();
         
         if(call == null) {
            throw new IllegalStateException("Method '" + name + "' not found for " + type);
         }
         Result result = call.call();
         ResultFlow flow = result.getFlow();
         Object value = result.getValue();
         
         if(flow == ResultFlow.THROW) {
            throw new IllegalStateException("Method '" + name + "' for " + type + " had an exception");
         }
         return new Transient(value);
      }
   }
   
   private class ObjectHandler implements InvocationDispatcher {
      
      private final Object object;
      private final Scope scope;      
      
      public ObjectHandler(Scope scope, Object object) {
         this.object = object;
         this.scope = scope;
      }

      @Override
      public Value dispatch(String name, Object... arguments) throws Exception {
         Module module = scope.getModule();
         Context context = module.getContext();
         FunctionBinder binder = context.getBinder();
         Callable<Result> call = binder.bind(scope, object, name, arguments);
         Class type = object.getClass();
         
         if(call == null) {
            throw new IllegalStateException("Method '" + name + "' not found for " + type);
         }
         Result result = call.call();
         ResultFlow flow = result.getFlow();
         Object value = result.getValue();
         
         if(flow == ResultFlow.THROW) {
            throw new IllegalStateException("Method '" + name + "' for " + type + " had an exception");
         }
         return new Transient(value);
      }
   }
   
   private class LocalHandler implements InvocationDispatcher {
      
      private final Scope scope;      
      
      public LocalHandler(Scope scope) {
         this.scope = scope;
      }

      @Override
      public Value dispatch(String name, Object... arguments) throws Exception {
         Module module = scope.getModule();
         Context context = module.getContext();
         FunctionBinder binder = context.getBinder();
         Callable<Result> call = binder.bind(scope, module, name, arguments);
         
         if(call == null) {
            throw new IllegalStateException("Method '" + name + "' not found in scope");
         }
         Result result = call.call();
         ResultFlow flow = result.getFlow();
         Object value = result.getValue();
         
         if(flow == ResultFlow.THROW) {
            throw new IllegalStateException("Method '" + name + "' had an exception");
         }
         return new Transient(value);  
      }
      
   }
}