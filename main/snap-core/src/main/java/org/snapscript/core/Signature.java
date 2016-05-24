package org.snapscript.core;

import java.util.List;

public class Signature {
   
   private final List<Parameter> parameters;
   private final boolean variable;

   public Signature(List<Parameter> parameters){
      this(parameters, false);
   }
   
   public Signature(List<Parameter> parameters, boolean variable){
      this.parameters = parameters;
      this.variable = variable;
   }
   
   public List<Parameter> getParameters(){
      return parameters;
   }
   
   public boolean isVariable() {
      return variable;
   }
}