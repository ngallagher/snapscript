package org.snapscript.core;

import java.util.List;

public class Type {
   
   private final List<Property> properties;
   private final List<Function> functions;
   private final List<Type> types;
   private final Class type;
   private final Type entry;
   private final String module;
   private final String name;
   
   public Type(String name, String module, Type entry, List<Type> types, List<Property> properties, List<Function> functions){
      this(name, module, entry, types, properties, functions, null);
   }
   
   public Type(String name, String module, Type entry, List<Type> types, List<Property> properties, List<Function> functions, Class type){
      this.functions = functions;
      this.properties = properties;
      this.module = module;
      this.entry = entry;
      this.types = types;
      this.type = type;
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
   
   public Class getType() {
      return type;
   }
   
   public Type getEntry(){
      return entry;
   }
   
   public String getModule(){
      return module;
   }
   
   public String getName(){
      return name;
   }
   
   @Override
   public String toString() {
      return name;
   }
}
