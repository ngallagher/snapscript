package org.snapscript.compile.instruction;

import org.snapscript.core.Type;

public class Parameter {
   
   private final String name;
   private final Type type;
   private final boolean variable;
   
   public Parameter(String name, Type type){
      this(name, type, false);
   }
   
   public Parameter(String name, Type type, boolean variable){
      this.variable = variable;
      this.name = name;
      this.type = type;
   }
   
   public boolean isVariable() {
      return variable;
   }
   
   public String getName() {
      return name;
   }
   
   public Type getType() {
      return type;
   }
}  