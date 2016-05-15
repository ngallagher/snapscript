package org.snapscript.core;

import java.util.ArrayList;
import java.util.List;

import org.snapscript.core.Function;
import org.snapscript.core.Module;
import org.snapscript.core.Property;
import org.snapscript.core.Type;

public class TestType implements Type {

   private final List<Annotation> annotations;
   private final List<Property> properties;
   private final List<Function> functions;
   private final List<Type> types;
   private final Module module;
   private final Class type;
   private final Type entry;
   private final String name;

   public TestType(Module module, String name, Type entry, Class type){
      this.annotations = new ArrayList<Annotation>();
      this.properties = new ArrayList<Property>();
      this.functions = new ArrayList<Function>();
      this.types = new ArrayList<Type>();
      this.module = module;
      this.entry = entry;
      this.type = type;
      this.name = name;
   }
   
   public List<Annotation> getAnnotations() {
      return annotations;
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
