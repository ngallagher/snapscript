package org.snapscript.compile.instruction.define;

import org.snapscript.compile.instruction.Constraint;
import org.snapscript.compile.instruction.DeclareBlank;
import org.snapscript.compile.instruction.DeclareProperty;
import org.snapscript.compile.instruction.ModifierChecker;
import org.snapscript.compile.instruction.ModifierList;
import org.snapscript.compile.instruction.literal.TextLiteral;
import org.snapscript.core.Evaluation;
import org.snapscript.core.Initializer;

public class MemberFieldDeclaration {
   
   private final ModifierChecker checker;
   private final TextLiteral identifier;
   private final Constraint constraint;
   private final Evaluation value;

   public MemberFieldDeclaration(ModifierList modifiers, TextLiteral identifier, Constraint constraint, Evaluation value) {
      this.checker = new ModifierChecker(modifiers);
      this.constraint = constraint;
      this.identifier = identifier;
      this.value = value;
   }
   
   public Initializer declare(Initializer initializer) throws Exception {
      Evaluation evaluation = create(initializer);

      if (checker.isStatic()) {
         return new StaticFieldInitializer(evaluation);
      }
      return new InstanceFieldInitializer(evaluation);
   }
 
   private Evaluation create(Initializer initializer) throws Exception {
      if (checker.isConstant()) {
         return new DeclareBlank(identifier, constraint, value);
      }
      return new DeclareProperty(identifier, constraint, value);
   }
}
