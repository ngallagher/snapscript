package org.snapscript.core.index;

import static org.snapscript.core.ModifierType.CONSTANT;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.snapscript.core.ModifierType;
import org.snapscript.core.PrimitivePromoter;
import org.snapscript.core.Property;
import org.snapscript.core.Type;

public class PropertyIndexer {
   
   private final ModifierConverter converter;
   private final PropertyGenerator generator;
   private final PrimitivePromoter promoter;
   private final TypeIndexer indexer;
   
   public PropertyIndexer(TypeIndexer indexer){
      this.converter = new ModifierConverter();
      this.generator = new PropertyGenerator();
      this.promoter = new PrimitivePromoter();
      this.indexer = indexer;
   }

   public List<Property> index(Class source) throws Exception {
      Method[] methods = source.getDeclaredMethods();
      Field[] fields = source.getDeclaredFields();

      if(fields.length > 0 || methods.length > 0) {
         List<Property> properties = new ArrayList<Property>();
         Set<String> done = new HashSet<String>();
         
         for(Field field : fields) {
            int modifiers = converter.convert(field);
            
            if(ModifierType.isPublic(modifiers)) {
               String name = field.getName();
               Class declaration = field.getType();
               Type type = indexer.loadType(declaration);
               Property property = generator.generate(field, type, name, modifiers); 
               
               properties.add(property);
            }
         }
         for(Method method : methods){
            int modifiers = converter.convert(method);
            
            if(ModifierType.isPublic(modifiers) && !ModifierType.isStatic(modifiers)) {
               Class[] parameters = method.getParameterTypes();
               
               if(parameters.length == 0) {
                  String name = PropertyType.getPropertyName(method);
                  
                  if(done.add(name)){
                     Class declaration = method.getReturnType();
                     Method write = match(methods, declaration, name);
                     
                     if(write == null) {
                        modifiers |= CONSTANT.mask;
                     }
                     Class normal = promoter.promote(declaration);
                     Type type = indexer.loadType(normal);
                     Property property = generator.generate(method, write, type, name, modifiers);                
                     
                     if(write != null){
                        write.setAccessible(true);
                     }
                     method.setAccessible(true);
                     properties.add(property);
                  }
               }
            }
         }
         return properties;
      }
      return Collections.emptyList();
   }
   
   private Method match(Method[] methods, Class require, String name) throws Exception {
      PropertyType[] types = PropertyType.values();

      for(Method method : methods) {         
         int modifiers = converter.convert(method);
         
         if(!ModifierType.isStatic(modifiers) && ModifierType.isPublic(modifiers)) {
            for(PropertyType type : types) {            
               if(type.isWrite(method)) {
                  Class[] parameters = method.getParameterTypes();
                  Class actual = parameters[0];
                  
                  if(actual == require) {
                     String property = type.getProperty(method);
      
                     if(property.equalsIgnoreCase(name)) {
                        return method;
                     }
                  }
               }
            }
         }
      }
      return null;
   }
}
