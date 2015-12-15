package org.snapscript.compile.instruction;

import org.snapscript.core.Bug;
import org.snapscript.core.Reference;
import org.snapscript.core.Scope;
import org.snapscript.core.State;
import org.snapscript.core.Type;
import org.snapscript.core.Value;

public class DeclareVariable implements Evaluation {
   
   private final DeclarationConverter checker;
   private final TextLiteral identifier;
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
      this.checker = new DeclarationConverter(constraint);
      this.identifier = identifier;
      this.value = value;
   }   

   @Override
   public Value evaluate(Scope scope, Object left) throws Exception {
      Value variable = identifier.evaluate(scope, null);
      String name = variable.getString();
      Value value = create(scope, name);

      return declare(scope, value, name);
   }
   
   protected Value create(Scope scope, String name) throws Exception {
      Object object = null;
      
      if(value != null) {         
         Value result = value.evaluate(scope, null);         
         
         if(result != null) {
            object = result.getValue();
         }
      }
      return checker.convert(scope, object, name);
   }
   
   @Bug("Maybe a Value.getReference or ValueType.getValue would be good here rather than new Reference(..)")
   protected Value declare(Scope scope, Value value, String name) throws Exception {
      Object object = value.getValue();
      Type type = value.getConstraint();
      State state = scope.getState();
      
      try {      
         Reference reference = new Reference(object, type, name);
         state.addVariable(name, reference);
         return reference;
      }catch(Exception e) {
         throw new IllegalStateException("Declaration of variable '" + name +"' failed", e);
      }      
   }
}