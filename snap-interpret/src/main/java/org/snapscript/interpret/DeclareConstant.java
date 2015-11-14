package org.snapscript.interpret;

import org.snapscript.core.Constant;
import org.snapscript.core.State;
import org.snapscript.core.Value;

public class DeclareConstant extends DeclareVariable {

   public DeclareConstant(TextLiteral identifier) {
      this(identifier, null, null);
   }
   
   public DeclareConstant(TextLiteral identifier, Constraint constraint) {      
      this(identifier, constraint, null);
   }
   
   public DeclareConstant(TextLiteral identifier, Evaluation value) {
      this(identifier, null, value);
   }
   
   public DeclareConstant(TextLiteral identifier, Constraint constraint, Evaluation value) {
      super(identifier, constraint, value);
   }
   
   @Override
   protected Value declare(State state, String name, Object object) throws Exception {
      Constant constant = new Constant(object);
      
      try {
         state.addConstant(name, constant);
      } catch(Exception e) {
         throw new IllegalStateException("Declaration of constant '" + name + "' failed");
      }
      return constant;
   }   
}