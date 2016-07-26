package org.snapscript.compile.instruction.define;

import org.snapscript.compile.instruction.Constraint;
import org.snapscript.compile.instruction.DeclareBlank;
import org.snapscript.compile.instruction.literal.TextLiteral;
import org.snapscript.core.Evaluation;
import org.snapscript.core.Initializer;

public class TraitConstantDeclaration {
   
   private final TextLiteral identifier;
   private final Constraint constraint;
   private final Evaluation value;

   public TraitConstantDeclaration(TextLiteral identifier, Constraint constraint, Evaluation value) {
      this.constraint = constraint;
      this.identifier = identifier;
      this.value = value;
   }
   
   public Initializer declare(Initializer initializer) throws Exception {
      Evaluation evaluation = new DeclareBlank(identifier, constraint, value);
      return new StaticFieldInitializer(evaluation);
   }
}
