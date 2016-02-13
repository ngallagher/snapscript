package org.snapscript.core;

import java.util.List;

public class Signature {
   
   private final List<String> names;
   private final List<Type> types;
   private final boolean variable;

   public Signature(List<String> names, List<Type> types){
      this(names, types, false);
   }
   
   public Signature(List<String> names, List<Type> types, boolean variable){
      this.variable = variable;
      this.types = types;
      this.names = names;
   }
   
   public List<String> getNames(){
      return names;
   }
   
   public List<Type> getTypes(){
      return types;
   }
   
   public boolean isVariable() {
      return variable;
   }
}