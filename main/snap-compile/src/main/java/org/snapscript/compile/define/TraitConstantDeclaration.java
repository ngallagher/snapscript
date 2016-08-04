package org.snapscript.compile.define;

import org.snapscript.compile.constraint.Constraint;
import org.snapscript.compile.declare.DeclareBlank;
import org.snapscript.compile.literal.TextLiteral;
import org.snapscript.core.Evaluation;
import org.snapscript.core.define.Initializer;

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
