package org.snapscript.core;

import java.util.List;

public class Signature {
   
   private final List<Parameter> parameters;
   private final Type definition;
   private final boolean variable;

   public Signature(List<Parameter> parameters){
      this(parameters, false);
   }
   
   public Signature(List<Parameter> parameters, boolean variable){
      this.definition = new FunctionType(this);
      this.parameters = parameters;
      this.variable = variable;
   }
   
   public Type getDefinition() {
      return definition;
   }
   
   public List<Parameter> getParameters(){
      return parameters;
   }
   
   public boolean isVariable() {
      return variable;
   }
}