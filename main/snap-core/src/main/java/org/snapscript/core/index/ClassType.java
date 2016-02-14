package org.snapscript.core.index;

import java.util.ArrayList;
import java.util.List;

import org.snapscript.core.Function;
import org.snapscript.core.Module;
import org.snapscript.core.Property;
import org.snapscript.core.Type;

public class ClassType implements Type {

   private final List<Property> properties;
   private final List<Function> functions;
   private final List<Type> types;
   private final Module module;
   private final Class type;
   private final Type entry;
   private final String name;
   
   public ClassType(Module module, String name, Type entry){
      this(module, name, entry, null);
   }
   
   public ClassType(Module module, String name, Type entry, Class type){
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
   
   public Module getModule(){
      return module;
   }
   
   public Class getType() {
      return type;
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
