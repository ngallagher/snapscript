package org.snapscript.develop.complete;

import java.util.Collections;
import java.util.List;

import org.snapscript.core.Function;
import org.snapscript.core.Module;
import org.snapscript.core.Property;
import org.snapscript.core.Type;

public class CompletionType {

   private final Object value;
   private final String name;
   
   public CompletionType(Type value, String name) {
      this.value = value;
      this.name = name;
   }
   
   public CompletionType(Module value, String name) {
      this.value = value;
      this.name = name;
   }
   
   public String getName() {
      return name;
   }
   
   public Class getType() {
      if(Type.class.isInstance(value)) {
         return ((Type)value).getType();
      }
      return null;
   }
   
   public List<Function> getFunctions(){
      if(Module.class.isInstance(value)) {
         return ((Module)value).getFunctions();
      }
      if(Type.class.isInstance(value)) {
         return ((Type)value).getFunctions();
      }
      return Collections.emptyList();
   }
   
   public List<Property> getProperties(){
      if(Type.class.isInstance(value)) {
         return ((Type)value).getProperties();
      }
      return Collections.emptyList();
   }
   
   public boolean isModule(){
      return Module.class.isInstance(value);
   }
   
   public boolean isType() {
      return Type.class.isInstance(value);
   }
   
   @Override
   public String toString() {
      return name;
   }
}
