package org.snapscript.compile.instruction;

import java.util.List;

import org.snapscript.core.Module;
import org.snapscript.core.Result;
import org.snapscript.core.Scope;
import org.snapscript.core.State;
import org.snapscript.core.Statement;
import org.snapscript.core.Transient;
import org.snapscript.core.Type;
import org.snapscript.core.Value;

public class Declaration extends Statement {
   
   private final ConstraintChecker checker;
   private final Evaluation identifier;
   private final Constraint constraint;
   private final Evaluation value;
   
   public Declaration(Evaluation identifier) {
      this(identifier, null, null);
   }
   
   public Declaration(Evaluation identifier, Constraint constraint) {      
      this(identifier, constraint, null);
   }
   
   public Declaration(Evaluation identifier, Evaluation value) {
      this(identifier, null, value);
   }
   
   public Declaration(Evaluation identifier, Constraint constraint, Evaluation value) {
      this.checker = new ConstraintChecker();
      this.constraint = constraint;
      this.identifier = identifier;
      this.value = value;
   }
   
   @Override
   public Result execute(Scope scope) throws Exception {
      Value variable = identifier.evaluate(scope, null);
      String name = variable.getString();
      State state = scope.getState();
      
      if(value != null) {         
         Value result = value.evaluate(scope, null);         
         Object value = result.getValue();

         if(constraint != null && value != null) {
            Module module = scope.getModule();
            Value qualifier = constraint.evaluate(scope, null);
            String alias = qualifier.getString();
            Class type = value.getClass(); 
            Type require = module.getType(alias); 
            
            if(!checker.compatible(scope, value, require)) {
               throw new IllegalStateException("Constraint '" + require + "' does not match " + type);
            }
         }
         state.setValue(name, result);
      } else {
         Value constant = new Transient(null); // bit rubbish!!
         state.setValue(name, constant);
      }
      return new Result();
   }
}