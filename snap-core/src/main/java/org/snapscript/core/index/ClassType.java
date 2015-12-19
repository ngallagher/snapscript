package org.snapscript.core.index;

import java.util.ArrayList;
import java.util.List;

import org.snapscript.core.Bug;
import org.snapscript.core.Function;
import org.snapscript.core.Property;
import org.snapscript.core.Type;

public class ClassType implements Type {

   private final List<Property> properties;
   private final List<Function> functions;
   private final List<Type> types;
   private final Class type;
   private final Type entry;
   private final String module;
   private final String name;
   
   public ClassType(String name, String module, Type entry){
      this(name, module, entry, null);
   }
   
   @Bug("Needs some information regarding the type of type, for example enum, trait")
   public ClassType(String name, String module, Type entry, Class type){
      this.properties = new ArrayList<Property>();
      this.functions = new ArrayList<Function>();
      this.types = new ArrayList<Type>();
      this.module = module;
      this.entry = entry;
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
