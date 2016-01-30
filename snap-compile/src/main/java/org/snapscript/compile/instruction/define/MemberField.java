package org.snapscript.compile.instruction.define;

import java.util.List;

import org.snapscript.compile.instruction.Constraint;
import org.snapscript.compile.instruction.DeclareConstant;
import org.snapscript.compile.instruction.DeclareVariable;
import org.snapscript.compile.instruction.Evaluation;
import org.snapscript.compile.instruction.TextLiteral;
import org.snapscript.core.Bug;
import org.snapscript.core.Initializer;
import org.snapscript.core.ModifierType;
import org.snapscript.core.Property;
import org.snapscript.core.Scope;
import org.snapscript.core.ScopeAccessor;
import org.snapscript.core.StaticAccessor;
import org.snapscript.core.Type;
import org.snapscript.core.Value;

public class MemberField implements TypePart {

   private final ModifierChecker checker;
   private final TextLiteral identifier;
   private final Constraint constraint;
   private final Evaluation value;

   public MemberField(ModifierList modifiers, TextLiteral identifier) {
      this(modifiers, identifier, null, null);
   }

   public MemberField(ModifierList modifiers, TextLiteral identifier, Constraint constraint) {
      this(modifiers, identifier, constraint, null);
   }

   public MemberField(ModifierList modifiers, TextLiteral identifier, Evaluation value) {
      this(modifiers, identifier, null, value);
   }

   public MemberField(ModifierList modifiers, TextLiteral identifier, Constraint constraint, Evaluation value) {
      this.checker = new ModifierChecker(modifiers);
      this.constraint = constraint;
      this.identifier = identifier;
      this.value = value;
   }

   @Bug("This is rubbish and needs to be cleaned up")
   @Override
   public Initializer define(Scope scope, Initializer statements, Type type) throws Exception {
      List<Property> properties = type.getProperties();
      Value vvvv = identifier.evaluate(scope, null);
      String name = vvvv.getString();

      if (checker.isStatic()) {
         Initializer initializer = null;
         if (checker.isConstant()) {
            Evaluation evaluation = new DeclareConstant(identifier, constraint, value);
            initializer = new StaticInitializer(evaluation, scope);
         } else {
            Evaluation evaluation = new DeclareVariable(identifier, constraint, value);
            initializer = new StaticInitializer(evaluation, scope);
         }
         StaticAccessor accessor = new StaticAccessor(statements, scope, type, name);
         Property property = new Property(name, type, accessor);

         properties.add(property);

         return initializer;
      }
      ScopeAccessor accessor = new ScopeAccessor(name);
      Property property = new Property(name, type, accessor);

      properties.add(property);

      if (checker.isConstant()) {
         Evaluation evaluation = new DeclareConstant(identifier, constraint, value);
         return new InstanceInitializer(evaluation, type);
      }
      Evaluation evaluation = new DeclareVariable(identifier, constraint, value);
      return new InstanceInitializer(evaluation, type);

   }
}