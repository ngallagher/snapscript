package org.snapscript.compile.instruction.variable;

import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import org.snapscript.core.Property;
import org.snapscript.core.PropertyValue;
import org.snapscript.core.Scope;
import org.snapscript.core.Type;
import org.snapscript.core.TypeTraverser;
import org.snapscript.core.Value;

public class TypeResolver implements ValueResolver<Type> {
   
   private final AtomicReference<Property> reference;
   private final TypeTraverser traverser;
   private final ObjectResolver resolver;
   private final String name;
   
   public TypeResolver(TypeTraverser extractor, String name) {
      this.resolver = new ObjectResolver(extractor, name);
      this.reference = new AtomicReference<Property>();
      this.traverser = extractor;
      this.name = name;
   }
   
   @Override
   public Value resolve(Scope scope, Type left) {
      Property property = reference.get();
      
      if(property == null) {
         Set<Type> list = traverser.traverse(left);
         
         for(Type base : list) {
            Property match = resolver.match(scope, left, base);
            
            if(match != null) {
               reference.set(match);
               return new PropertyValue(match, left, name);
            }
         } 
         Property match = resolver.match(scope, left);
         
         if(match != null) {
            reference.set(match);
            return new PropertyValue(property, left, name);
         }
         return null;
      } 
      return new PropertyValue(property, left, name);
   }
}