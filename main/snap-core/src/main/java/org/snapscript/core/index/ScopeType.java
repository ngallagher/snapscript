package org.snapscript.core.index;

import java.util.ArrayList;
import java.util.List;

import org.snapscript.core.Annotation;
import org.snapscript.core.Function;
import org.snapscript.core.Module;
import org.snapscript.core.ModuleRegistry;
import org.snapscript.core.Property;
import org.snapscript.core.Type;

public class ScopeType implements Type {
   
   private final List<Annotation> annotations;
   private final List<Property> properties;
   private final List<Function> functions;
   private final ModuleRegistry registry;
   private final List<Type> types;
   private final String module;
   private final String name;
   
   public ScopeType(ModuleRegistry registry, String module, String name){
      this.annotations = new ArrayList<Annotation>();
      this.properties = new ArrayList<Property>();
      this.functions = new ArrayList<Function>();
      this.types = new ArrayList<Type>();
      this.registry = registry;
      this.module = module;
      this.name = name;
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
      return registry.addModule(module);
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
   public String toString() {
      return name;
   }
}
