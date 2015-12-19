package org.snapscript.core.index;

import java.util.ArrayList;
import java.util.List;

import org.snapscript.core.Bug;
import org.snapscript.core.Function;
import org.snapscript.core.Property;
import org.snapscript.core.Type;

public class ScopeType implements Type {
   
   private final List<Property> properties;
   private final List<Function> functions;
   private final List<Type> types;
   private final String module;
   private final String name;
   
   public ScopeType(String name, String module){
      this.properties = new ArrayList<Property>();
      this.functions = new ArrayList<Function>();
      this.types = new ArrayList<Type>();
      this.module = module;
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
   
   public String getModule(){
      return module;
   }
   
   public String getName(){
      return name;
   }
   
   public Class getType() {
      return null;
   }
   
   public Type getEntry(){
      return null;
   }
   
   @Override
   public String toString() {
      return name;
   }
}
