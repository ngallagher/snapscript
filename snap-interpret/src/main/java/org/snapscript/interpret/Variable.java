package org.snapscript.interpret;

import org.snapscript.core.Scope;
import org.snapscript.core.Value;

public class Variable implements Evaluation {
   
   private final VariableResolver resolver;
   
   public Variable(Evaluation identifier) {
      this.resolver = new VariableResolver(identifier);
   }
   
   @Override
   public Value evaluate(Scope scope, Object left) throws Exception{
      return resolver.resolve(scope, left);
   }  
}