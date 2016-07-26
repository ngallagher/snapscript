package org.snapscript.compile.instruction.define;

import static org.snapscript.core.Reserved.TYPE_CONSTRUCTOR;

import org.snapscript.compile.instruction.ArgumentList;
import org.snapscript.compile.instruction.literal.TextLiteral;
import org.snapscript.core.Evaluation;
import org.snapscript.core.Initializer;
import org.snapscript.core.InternalStateException;
import org.snapscript.core.SuperExtractor;
import org.snapscript.core.Type;
import org.snapscript.parse.StringToken;

public class SuperConstructor implements TypePart {
   
   private final SuperExtractor extractor;
   private final ArgumentList arguments;
   
   public SuperConstructor() {
      this(null);
   }
   
   public SuperConstructor(ArgumentList arguments) {
      this.extractor = new SuperExtractor();
      this.arguments = arguments;
   }

   @Override
   public Initializer compile(Initializer initializer, Type type) throws Exception {
      Type base = extractor.extractor(type);
      
      if(base == null) {
         throw new InternalStateException("No super type for '" + type + "'");
      }     
      return define(initializer, base);
   }
   
   protected Initializer define(Initializer statements, Type type) throws Exception {
      StringToken name = new StringToken(TYPE_CONSTRUCTOR);
      Evaluation literal = new TextLiteral(name);
      Evaluation evaluation = new SuperInvocation(literal, arguments, type);
      
      return new SuperInitializer(evaluation, type);
   }
}
