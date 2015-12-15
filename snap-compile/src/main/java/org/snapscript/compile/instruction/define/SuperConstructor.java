package org.snapscript.compile.instruction.define;

import static org.snapscript.core.Reserved.TYPE_CONSTRUCTOR;
import static org.snapscript.core.Reserved.TYPE_SUPER;

import java.util.List;

import org.snapscript.compile.instruction.ArgumentList;
import org.snapscript.compile.instruction.Evaluation;
import org.snapscript.core.Constant;
import org.snapscript.core.Initializer;
import org.snapscript.core.Result;
import org.snapscript.core.ResultType;
import org.snapscript.core.Scope;
import org.snapscript.core.State;
import org.snapscript.core.SuperScope;
import org.snapscript.core.Transient;
import org.snapscript.core.Type;
import org.snapscript.core.Value;

public class SuperConstructor implements TypePart {
   
   private final ArgumentList list;
   
   public SuperConstructor(ArgumentList list) {
      this.list = list;
   }

   @Override
   public Initializer define(Scope scope, Initializer statement, Type type) throws Exception {
      List<Type> types=type.getTypes();
         Type superT=types.isEmpty()?null:types.get(0);
      
      if(superT == null) {
         throw new IllegalStateException("No super constructor exists");
      }     
      Name name = new Name();
      Evaluation evaluation= new SuperFunction(name, superT,list);
      return new SuperStatement(evaluation, superT);
   }
   public final class Name implements Evaluation {

      @Override
      public Value evaluate(Scope scope, Object left) throws Exception {
         return new Transient(TYPE_CONSTRUCTOR);
      }
      
   }
   public final class SuperStatement extends Initializer {
      
      private final Evaluation expression;
      private final Type type;
      
      public SuperStatement(Evaluation expression, Type type) {
         this.expression = expression;
         this.type = type;
      }

      @Override
      public Result execute(Scope scope, Type real) throws Exception {
         Value reference = expression.evaluate(scope, real);
         Scope value = reference.getValue();
         
         // This won't work, there needs to be two forms of binding, a special Value needs to be 
         // assigned to super so that it takes a unique path through the FunctionBinder, perhaps
         // there needs to be a parameter we can pass that says when a method is referenced as
         // super then it needs to bind to a 
         
         Scope compound = new SuperScope(value, real, type); // this is a scope that sits between the instance and its super instance!!! kind of CRAP!!
         Constant constant = new Constant(compound, TYPE_SUPER);
         State state = compound.getState();
         state.addConstant(TYPE_SUPER, constant);
         return ResultType.getNormal(compound);
      }
   }
}
