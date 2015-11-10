package org.snapscript.interpret.define;

import java.util.List;

import org.snapscript.core.Holder;
import org.snapscript.core.Initializer;
import org.snapscript.core.Result;
import org.snapscript.core.ResultFlow;
import org.snapscript.core.Scope;
import org.snapscript.core.Type;
import org.snapscript.core.Value;
import org.snapscript.interpret.ArgumentList;
import org.snapscript.interpret.Evaluation;

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
      return new SuperStatement(evaluation);
   }
   public final class Name implements Evaluation {

      @Override
      public Value evaluate(Scope scope, Object left) throws Exception {
         return new Holder("new");
      }
      
   }
   public final class SuperStatement implements Initializer {
      
      private final Evaluation expression;
      
      public SuperStatement(Evaluation expression) {
         this.expression = expression;
      }

      @Override
      public Result initialize(Scope scope, Type type) throws Exception {
         Value reference = expression.evaluate(scope, type);
         Object value = reference.getValue();
         
         return new Result(ResultFlow.NORMAL, value);
      }
   }
}
