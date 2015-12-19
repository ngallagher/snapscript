package org.snapscript.compile.instruction;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

import org.snapscript.core.Accessor;
import org.snapscript.core.Bug;
import org.snapscript.core.Context;
import org.snapscript.core.HierarchyExtractor;
import org.snapscript.core.Module;
import org.snapscript.core.ModuleBuilder;
import org.snapscript.core.Property;
import org.snapscript.core.PropertyValue;
import org.snapscript.core.Scope;
import org.snapscript.core.State;
import org.snapscript.core.Type;
import org.snapscript.core.Value;
import org.snapscript.core.ValueType;

@Bug("This should be cleaned up")
public class VariableResolver {
   
   private final Map<Object, ValueResolver> resolvers;
   private final HierarchyExtractor extractor;
   private final VariableKeyBuilder builder;
   private final Evaluation identifier;
   
   public VariableResolver(Evaluation identifier) {
      this.resolvers = new ConcurrentHashMap<Object, ValueResolver>();
      this.extractor = new HierarchyExtractor();
      this.builder = new VariableKeyBuilder();
      this.identifier = identifier;
   }
   
   public Value resolve(Scope scope, Object left) throws Exception {
      Value reference = identifier.evaluate(scope, left);
      String name = reference.getString();
      Object key = builder.create(left, name);
      ValueResolver resolver = resolvers.get(key);
      
      if(resolver == null) { // XXX is this really right?? 
         resolver = match(left, name);
         resolvers.put(key, resolver);
      }
      Value value = resolver.resolve(scope, left);
      
      if(value == null) {
         throw new IllegalStateException("Could not resolve '" + name +"' in scope");
      }
      return value;
   }
   
   private ValueResolver match(Object left, String name) {
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
                  return ValueType.getTransient(result);
               }
               return null;
            }
            return ValueType.getTransient(type);
         }
         return variable;
      }
   }
   
   private class TypeResolver implements ValueResolver<Type> {
      
      private final AtomicReference<Accessor> reference;
      private final ObjectResolver resolver;
      private final String name;
      
      public TypeResolver(String name) {
         this.reference = new AtomicReference<Accessor>();
         this.resolver = new ObjectResolver(name);
         this.name = name;
      }
      
      @Bug("This is totally wrong, needed for X.class.functions and MyEnum.VAL, which are different really! consider enums also here!!")
      @Override
      public Value resolve(Scope scope, Type left) {
         Accessor accessor = reference.get();
         
         if(accessor == null) {
            Set<Type> list = extractor.extract(left);
            
            for(Type base : list) {
               Accessor match = resolver.resolve(scope, left, base);
               
               if(match != null) {
                  reference.set(match);
                  return new PropertyValue(match, left, name);
               }
            } 
            return resolver.resolve(scope, left);
         }
         return new PropertyValue(accessor, left, name);
      }
   }
   
   private class ObjectResolver implements ValueResolver<Object> {
      
      private final AtomicReference<Accessor> reference;
      private final String name;
      
      public ObjectResolver(String name) {
         this.reference = new AtomicReference<Accessor>();
         this.name = name;
      }
      
      @Override
      public Value resolve(Scope scope, Object left) {
         Accessor accessor = reference.get();
         
         if(accessor == null) {
            Module module = scope.getModule();
            Class type = left.getClass();
            String alias = type.getName();
            Type source = module.getType(alias);
            Set<Type> list = extractor.extract(source);
            
            for(Type base : list) {
               Accessor match = resolve(scope, left, base);
               
               if(match != null) {
                  reference.set(match);
                  return new PropertyValue(match, left, name);
               }
            }
            return null;
         }
         return new PropertyValue(accessor, left, name);
      }
      
      public Accessor resolve(Scope scope, Object left, Type type) {
         List<Property> properties = type.getProperties();
         
         for(Property property : properties){
            String field = property.getName();
            
            if(field.equals(name)) {
               Accessor accessor = property.getAccessor();    
               
               if(accessor != null) {
                  return accessor;
               }
            }
         } 
         return null;
      }
      
   }
}
