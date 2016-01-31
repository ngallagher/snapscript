package org.snapscript.compile.instruction.variable;

import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import org.snapscript.core.Accessor;
import org.snapscript.core.TypeTraverser;
import org.snapscript.core.Module;
import org.snapscript.core.Property;
import org.snapscript.core.PropertyValue;
import org.snapscript.core.Scope;
import org.snapscript.core.Type;
import org.snapscript.core.Value;

public class ObjectResolver implements ValueResolver<Object> {
   
   private final AtomicReference<Accessor> reference;
   private final TypeTraverser traverser;
   private final String name;
   
   public ObjectResolver(TypeTraverser extractor, String name) {
      this.reference = new AtomicReference<Accessor>();
      this.traverser = extractor;
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
         Set<Type> list = traverser.traverse(source);
         
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