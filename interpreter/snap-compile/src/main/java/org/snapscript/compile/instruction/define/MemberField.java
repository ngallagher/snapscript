package org.snapscript.compile.instruction.define;

import java.util.List;

import org.snapscript.compile.instruction.Constraint;
import org.snapscript.compile.instruction.ConstraintExtractor;
import org.snapscript.compile.instruction.Evaluation;
import org.snapscript.compile.instruction.literal.TextLiteral;
import org.snapscript.core.Accessor;
import org.snapscript.core.AccessorProperty;
import org.snapscript.core.Initializer;
import org.snapscript.core.Property;
import org.snapscript.core.Scope;
import org.snapscript.core.ScopeAccessor;
import org.snapscript.core.StaticAccessor;
import org.snapscript.core.Type;
import org.snapscript.core.Value;

public class MemberField implements TypePart {

   private final MemberFieldDeclaration declaration;
   private final ConstraintExtractor extractor;
   private final ModifierChecker checker;
   private final TextLiteral identifier;
   private final ModifierList list;
   
   public MemberField(ModifierList list, TextLiteral identifier) {
      this(list, identifier, null, null);
   }

   public MemberField(ModifierList list, TextLiteral identifier, Constraint constraint) {
      this(list, identifier, constraint, null);
   }

   public MemberField(ModifierList list, TextLiteral identifier, Evaluation value) {
      this(list, identifier, null, value);
   }

   public MemberField(ModifierList list, TextLiteral identifier, Constraint constraint, Evaluation value) {
      this.declaration = new MemberFieldDeclaration(list, identifier, constraint, value);
      this.extractor = new ConstraintExtractor(constraint);
      this.checker = new ModifierChecker(list);
      this.identifier = identifier;
      this.list = list;
   }

   @Override
   public Initializer define(Scope scope, Initializer initializer, Type type) throws Exception {
      Initializer declare = declaration.declare(scope, initializer);
      List<Property> properties = type.getProperties();
      Value value = identifier.evaluate(scope, null);
      Type constraint = extractor.extract(scope);
      String name = value.getString();
      int modifiers = list.getModifiers();
      
      if (checker.isStatic()) {
         Accessor accessor = new StaticAccessor(initializer, scope, type, name);
         Property property = new AccessorProperty(name, constraint, accessor, modifiers);
         
         properties.add(property);
      } else {
         Accessor accessor = new ScopeAccessor(name);
         Property property = new AccessorProperty(name, constraint, accessor, modifiers); // is this the correct type!!??
         
         properties.add(property);
      }
      return declare;
   }
}