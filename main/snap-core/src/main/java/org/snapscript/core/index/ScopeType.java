package org.snapscript.core.index;

import java.util.ArrayList;
import java.util.List;

import org.snapscript.core.Annotation;
import org.snapscript.core.Function;
import org.snapscript.core.Module;
import org.snapscript.core.Property;
import org.snapscript.core.Type;

public class ScopeType implements Type {
   
   private final List<Annotation> annotations;
   private final List<Property> properties;
   private final List<Function> functions;
   private final List<Type> types;
   private final Module module;
   private final String name;
   private final int order;
   
   public ScopeType(Module module, String name, int order){
      this.annotations = new ArrayList<Annotation>();
      this.properties = new ArrayList<Property>();
      this.functions = new ArrayList<Function>();
      this.types = new ArrayList<Type>();
      this.module = module;
      this.name = name;
      this.order = order;
   }
   
   @Override
   public List<Annotation> getAnnotations() {
      return annotations;
   }
   
   @Override
   public List<Property> getProperties() {
      return properties;
   }
   
   @Override
   public List<Function> getFunctions(){
      return functions;
   }
   
   @Override
   public List<Type> getTypes(){
      return types;
   }
   
   @Override
   public Module getModule(){
      return module;
   }
   
   @Override
   public String getName(){
      return name;
   }
   
   @Override
   public Class getType() {
      return null;
   }
   
   @Override
   public Type getEntry(){
      return null;
   }
   
   @Override
   public int getOrder() {
      return order;
   }
   
   @Override
   public String toString() {
      return name;
   }
}
