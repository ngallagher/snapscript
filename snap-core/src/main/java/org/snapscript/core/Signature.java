package org.snapscript.core;

import java.util.List;

public class Signature {
   
   private final List<String> names;
   private final List<Type> types;
   private final int modifiers;
   
   public Signature(List<String> names, List<Type> types, int modifiers){
      this.modifiers = modifiers;
      this.types = types;
      this.names = names;
   }
   
   public List<String> getNames(){
      return names;
   }
   
   public List<Type> getTypes(){
      return types;
   }
   
   public int getModifiers() {
      return modifiers;
   }
}