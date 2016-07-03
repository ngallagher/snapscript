package org.snapscript.core;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class MapProperty implements Property<Map> {

   private final Type type;
   private final String name;
   private final int modifiers;
   
   public MapProperty(String name, Type type, int modifiers){
      this.modifiers = modifiers;
      this.name = name;
      this.type = type;
   }
   
   @Override
   public List<Annotation> getAnnotations(){
      return Collections.emptyList();
   }
   
   @Override
   public Type getType(){
      return type;
   }
   
   @Override
   public Type getConstraint() {
      return null;
   }
   
   @Override
   public String getName(){
      return name;
   }
   
   @Override
   public int getModifiers() {
      return modifiers;
   }
   
   @Override
   public Object getValue(Map source) {
      return source.get(name);
   }

   @Override
   public void setValue(Map source, Object value) {
      source.put(name, value);
   }
   
   @Override
   public String toString(){
      return name;
   }
}
