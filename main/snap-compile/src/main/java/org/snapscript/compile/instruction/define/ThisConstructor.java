package org.snapscript.compile.instruction.define;

import org.snapscript.compile.instruction.ArgumentList;
import org.snapscript.compile.instruction.Evaluation;
import org.snapscript.compile.instruction.construct.ConstructObject;
import org.snapscript.core.Initializer;
import org.snapscript.core.Scope;
import org.snapscript.core.Type;
import org.snapscript.core.Value;
import org.snapscript.core.ValueType;

public class ThisConstructor implements TypePart {
   
   private final ArgumentList arguments;
   
   public ThisConstructor() {
      this(null);
   }
   
   public ThisConstructor(ArgumentList arguments) {
      this.arguments = arguments;
   }

   @Override
   public Initializer define(Scope scope, Initializer statement, Type type) throws Exception {  
      Evaluation name = new TypeReference(type);
      ConstructObject evaluation = new ConstructObject(name, arguments);
      
      return new ThisInitializer(evaluation);
   }
   
   private static class TypeReference implements Evaluation {
      
      private final Type type;
      
      public TypeReference(Type type) {
         this.type = type;
      }

      @Override
      public Value evaluate(Scope scope, Object left) throws Exception {
         String name = type.getName();
         return ValueType.getTransient(name);
      }
   }
}