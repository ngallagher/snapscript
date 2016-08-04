package org.snapscript.code.define;

import org.snapscript.code.ArgumentList;
import org.snapscript.code.construct.CreateObject;
import org.snapscript.core.Evaluation;
import org.snapscript.core.Scope;
import org.snapscript.core.Statement;
import org.snapscript.core.Type;
import org.snapscript.core.Value;
import org.snapscript.core.ValueType;
import org.snapscript.core.define.Initializer;

public class ThisConstructor implements TypePart {
   
   private final ArgumentList arguments;
   
   public ThisConstructor() {
      this(null);
   }
   
   public ThisConstructor(ArgumentList arguments) {
      this.arguments = arguments;
   }

   @Override
   public Initializer compile(Initializer initializer, Type type) throws Exception {  
      Statement statement = new StaticBody(initializer, type);
      Evaluation reference = new TypeValue(type);
      CreateObject evaluation = new CreateObject(reference, arguments);
      
      return new ThisInitializer(statement, evaluation);
   }
   
   private static class TypeValue implements Evaluation {
      
      private final Type type;
      
      public TypeValue(Type type) {
         this.type = type;
      }

      @Override
      public Value evaluate(Scope scope, Object left) throws Exception {
         return ValueType.getTransient(type);
      }
   }
}