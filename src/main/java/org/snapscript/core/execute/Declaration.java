package org.snapscript.core.execute;

import java.util.List;

import org.snapscript.core.Holder;
import org.snapscript.core.Module;
import org.snapscript.core.Scope;
import org.snapscript.core.Type;
import org.snapscript.core.Value;

public class Declaration extends Statement {
   
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
      this.constraint = constraint;
      this.identifier = identifier;
      this.value = value;
   }
   
   @Override
   public Result execute(Scope scope) throws Exception {
      Value variable = identifier.evaluate(scope, null);
      String name = variable.getString();
      
      if(value != null) {         
         Value result = value.evaluate(scope, null);         
         Object value = result.getValue();

         if(constraint != null && value != null) {
            Module module = scope.getModule();
            Value qualifier = constraint.evaluate(scope, null);
            String alias = qualifier.getString();
            Class type = value.getClass(); 
            Type actual = module.getType(type);
            Type require = module.getType(alias);
            
            if(actual != require) {
               List<Type> compatible = actual.getTypes();
               
               if(!compatible.contains(require)) {
                  throw new IllegalStateException("Constraint '" + require + "' does not match " + actual);
               }
            }
         }
         scope.setValue(name, result);
      } else {
         Holder constant = new Holder(null); // bit rubbish!!
         scope.setValue(name, constant);
      }
      return new Result();
   }
}