package org.snapscript.compile.instruction.define;

import org.snapscript.compile.instruction.Constraint;
import org.snapscript.compile.instruction.DeclareBlank;
import org.snapscript.compile.instruction.DeclareVariable;
import org.snapscript.compile.instruction.ModifierChecker;
import org.snapscript.compile.instruction.literal.TextLiteral;
import org.snapscript.core.Evaluation;
import org.snapscript.core.Initializer;
import org.snapscript.core.Scope;

public class MemberFieldDeclaration {
   
   private final ModifierChecker checker;
   private final TextLiteral identifier;
   private final Constraint constraint;
   private final Evaluation value;

   public MemberFieldDeclaration(MemberDeclaration modifiers, TextLiteral identifier, Constraint constraint, Evaluation value) {
      this.checker = new ModifierChecker(modifiers);
      this.constraint = constraint;
      this.identifier = identifier;
      this.value = value;
   }
   
   public Initializer declare(Scope scope, Initializer initializer) throws Exception {
      Evaluation evaluation = create(scope, initializer);

      if (checker.isStatic()) {
         return new StaticFieldInitializer(evaluation, scope);
      }
      return new InstanceFieldInitializer(evaluation);
   }
 
   private Evaluation create(Scope scope, Initializer initializer) throws Exception {
      if (checker.isConstant()) {
         return new DeclareBlank(identifier, constraint, value);
      }
      return new DeclareVariable(identifier, constraint, value);
   }
}
