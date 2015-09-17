package org.snapscript.core;

import java.util.List;

// need to know if its primitives for function matching as null not allowed for primitive!!
public class Type {
   
   private final List<Property> properties;
   private final List<Function> functions;
   private final List<Type> types;
   private final Type entry;
   private final String name;
   
   public Type(String name, Type entry, List<Type> types, List<Property> properties, List<Function> functions){
      this.functions = functions;
      this.properties = properties;
      this.entry = entry;
      this.types = types;
      this.name = name;
   }
   
   public List<Property> getProperties() {
      return properties;
   }
   
   public List<Function> getFunctions(){
      return functions;
   }
   
   public List<Type> getTypes(){
      return types;
   }
   
   public Type getEntry(){
      return entry;
   }
   
   public String getName(){
      return name;
   }
   
   @Override
   public String toString() {
      return name;
   }
}
