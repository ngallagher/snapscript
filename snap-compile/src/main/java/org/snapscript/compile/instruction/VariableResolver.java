package org.snapscript.compile.instruction;

import java.util.List;

import org.snapscript.core.Accessor;
import org.snapscript.core.Context;
import org.snapscript.core.Transient;
import org.snapscript.core.Module;
import org.snapscript.core.ModuleBuilder;
import org.snapscript.core.Property;
import org.snapscript.core.PropertyValue;
import org.snapscript.core.Scope;
import org.snapscript.core.State;
import org.snapscript.core.Type;
import org.snapscript.core.Value;

public class VariableResolver {
   
   private final Evaluation identifier;
   
   public VariableResolver(Evaluation identifier) {
      this.identifier = identifier;
   }
   
   public Value resolve(Scope scope, Object left) throws Exception {
      Value reference = identifier.evaluate(scope, left);
      String name = reference.getString();
      ValueResolver resolver = match(scope, left, name);
      Value value = resolver.resolve(scope, left);
      
      if(value == null) {
         throw new IllegalStateException("Could not resolve '" + name +"' in scope");
      }
      return value;
   }
   
   private ValueResolver match(Scope scope, Object left, String name) {
      if(left == null) {
         return new LocalResolver(name);
      }
      if(left instanceof Scope) {
         return new ScopeResolver(name);
      }
      if(left instanceof Type) {
         return new TypeResolver(name);
      }
      return new ObjectResolver(name);
   }
   
   private interface ValueResolver<T> {
      public Value resolve(Scope scope, T left);
   }
   
   private class ScopeResolver implements ValueResolver<Scope> {
      
      private final String name;
      
      public ScopeResolver(String name) {
         this.name = name;
      }
      
      @Override
      public Value resolve(Scope scope, Scope left) {
         State state = left.getState();
         return state.getValue(name);
      }
   }
   
   private class LocalResolver implements ValueResolver<Object> {
      
      private final String name;
      
      public LocalResolver(String name) {
         this.name = name;
      }
      
      @Override
      public Value resolve(Scope scope, Object left) {
         State state = scope.getState();
         Value variable = state.getValue(name);
         
         if(variable == null) { 
            Context context = scope.getContext();
            Module module = scope.getModule();
            Type type = module.getType(name);
            
            if(type == null) {
               ModuleBuilder builder = context.getBuilder();
               Object result = builder.resolve(name);
               
               if(result != null) {
                  return new Transient(result);
               }
               return null;
            }
            return new Transient(type);
         }
         return variable;
      }
   }
   
   private class TypeResolver implements ValueResolver<Type> {
      
      private final ObjectResolver resolver;
      private final String name;
      
      public TypeResolver(String name) {
         this.resolver = new ObjectResolver(name);
         this.name = name;
      }
      
      public Value resolve(Scope scope, Type left) {
         List<Property> properties = left.getProperties();
         
         for(Property property : properties){
            String field = property.getName();
            
            if(field.equals(name)) {
               Accessor accessor = property.getAccessor();    
               
               if(accessor != null) {
                  return new PropertyValue(accessor, left, name);
               }
            }
         }  
         return resolver.resolve(scope, left);
      }
   }
   
   private class ObjectResolver implements ValueResolver<Object> {
      
      private final String name;
      
      public ObjectResolver(String name) {
         this.name = name;
      }
      
      public Value resolve(Scope scope, Object left) {
         Module module = scope.getModule();
         Class type = left.getClass();
         String alias = type.getName();
         Type source = module.getType(alias);
         Value value = resolve(scope, left, source);
         
         if(value == null) {
            List<Type> list = source.getTypes();
            
            for(Type base : list) {
               Value result = resolve(scope, left, base);
               
               if(result != null) {
                  return result;
               }
            }
         }  
         return value;
      }
      
      public Value resolve(Scope scope, Object left, Type type) {
         List<Property> properties = type.getProperties();
         
         for(Property property : properties){
            String field = property.getName();
            
            if(field.equals(name)) {
               Accessor accessor = property.getAccessor();    
               
               if(accessor != null) {
                  return new PropertyValue(accessor, left, name);
               }
            }
         } 
         return null;
      }
      
   }
}
