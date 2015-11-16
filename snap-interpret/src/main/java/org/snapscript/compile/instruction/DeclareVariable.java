package org.snapscript.compile.instruction;

import org.snapscript.core.Reference;
import org.snapscript.core.Scope;
import org.snapscript.core.State;
import org.snapscript.core.Value;

public class DeclareVariable implements Evaluation {
   
   private final ConstraintChecker checker;
   private final TextLiteral identifier;
   private final Constraint constraint;
   private final Evaluation value;
   
   public DeclareVariable(TextLiteral identifier) {
      this(identifier, null, null);
   }
   
   public DeclareVariable(TextLiteral identifier, Constraint constraint) {      
      this(identifier, constraint, null);
   }
   
   public DeclareVariable(TextLiteral identifier, Evaluation value) {
      this(identifier, null, value);
   }
   
   public DeclareVariable(TextLiteral identifier, Constraint constraint, Evaluation value) {
      this.checker = new ConstraintChecker();
      this.constraint = constraint;
      this.identifier = identifier;
      this.value = value;
   }   

   @Override
   public Value evaluate(Scope scope, Object left) throws Exception {
      Value variable = identifier.evaluate(scope, null);
      String name = variable.getString();
      State state = scope.getState();
      
      if(value != null) {         
         Value result = value.evaluate(scope, null);         
         Object value = result.getValue();

         if(constraint != null && value != null) {
            Value qualifier = constraint.evaluate(scope, null);
            String alias = qualifier.getString();

            if(!checker.compatible(scope, value, alias)) {
               throw new IllegalStateException("Variable '" + name + "' does not match constraint '" + alias + "'");
            }
         }
         return declare(state, name, value);
      }
      return declare(state, name, null);
   }
   
   protected Value declare(State state, String name, Object value) throws Exception {
      Reference reference = new Reference(value);         
      
      try {
         state.addVariable(name, reference);
      } catch(Exception e) {
         throw new IllegalStateException("Declaration of variable '" + name + "' failed", e);
      }
      return reference;      
   }
}