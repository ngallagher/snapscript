package org.snapscript.compile.instruction;

import org.snapscript.core.Evaluation;
import org.snapscript.core.InternalStateException;
import org.snapscript.core.Module;
import org.snapscript.core.Scope;
import org.snapscript.core.Type;
import org.snapscript.core.Value;
import org.snapscript.core.ValueType;

public class Parameter implements Evaluation {
   
   private volatile NameExtractor extractor;
   private volatile Constraint constraint;
   private volatile Value value;
   
   public Parameter(Evaluation identifier){
      this(identifier, null);
   }
   
   public Parameter(Evaluation identifier, Constraint constraint){
      this.extractor = new NameExtractor(identifier);
      this.constraint = constraint;
   }

   @Override
   public Value evaluate(Scope scope, Object left) throws Exception {
      if(value == null) {
         value = create(scope, left);
      }
      return value;
   }
   
   private Value create(Scope scope, Object left) throws Exception {
      String name = extractor.extract(scope);
      
      if(constraint != null && name != null) {
         Value value = constraint.evaluate(scope, left);  
         String alias = value.getString();
         Module module = scope.getModule();
         Type type = module.getType(alias);
         
         if(type == null) {
            throw new InternalStateException("Constraint '" + alias + "' for '" +name + "' was not imported");
         }
         return ValueType.getTransient(name, type);
      }
      return ValueType.getTransient(name);
   }
}  