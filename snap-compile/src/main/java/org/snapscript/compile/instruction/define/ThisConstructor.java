package org.snapscript.compile.instruction.define;

import static org.snapscript.core.Reserved.TYPE_CONSTRUCTOR;

import org.snapscript.compile.instruction.ArgumentList;
import org.snapscript.compile.instruction.Evaluation;
import org.snapscript.compile.instruction.construct.ConstructObject;
import org.snapscript.core.Bug;
import org.snapscript.core.Initializer;
import org.snapscript.core.Result;
import org.snapscript.core.ResultType;
import org.snapscript.core.Scope;
import org.snapscript.core.Type;
import org.snapscript.core.Value;
import org.snapscript.core.ValueType;

public class ThisConstructor implements TypePart {
   
   private final ArgumentList list;
   
   public ThisConstructor() {
      this(null);
   }
   
   public ThisConstructor(ArgumentList list) {
      this.list = list;
   }

   @Bug("THIS Is Wrong, IT NEEDS TO RETURN A SCOPE This is rubbish and needs to be cleaned up")
   @Override
   public Initializer define(Scope scope, Initializer statement, Type type) throws Exception {  
      Name name = new Name(type);
      ConstructObject evaluation= new ConstructObject(name,list);
      return new ThisStatement(evaluation);
   }
   public final class Name implements Evaluation {
      
      private final Type type;
      
      public Name(Type type) {
         this.type = type;
      }

      @Override
      public Value evaluate(Scope scope, Object left) throws Exception {
         String name = type.getName();
         return ValueType.getTransient(name);
      }
      
   }
   public final class ThisStatement extends Initializer {
      
      private final Evaluation expression;
      
      public ThisStatement(Evaluation expression) {
         this.expression = expression;
      }

      @Override
      public Result execute(Scope instance, Type real) throws Exception {
         Value value = expression.evaluate(instance, instance);
         Scope result = value.getValue();
         
         return ResultType.getNormal(result); // this will return the instance created!!
      }
   }
}