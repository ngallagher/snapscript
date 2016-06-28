package org.snapscript.compile.instruction.define;

import java.util.List;

import org.snapscript.compile.instruction.AnnotationList;
import org.snapscript.compile.instruction.Constraint;
import org.snapscript.compile.instruction.ConstraintExtractor;
import org.snapscript.compile.instruction.ModifierChecker;
import org.snapscript.compile.instruction.ModifierList;
import org.snapscript.compile.instruction.literal.TextLiteral;
import org.snapscript.core.Accessor;
import org.snapscript.core.AccessorProperty;
import org.snapscript.core.Evaluation;
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
   private final AnnotationList annotations;
   private final ModifierChecker checker;
   private final TextLiteral identifier;
   private final ModifierList list;
   
   public MemberField(AnnotationList annotations, ModifierList list, TextLiteral identifier) {
      this(annotations, list, identifier, null, null);
   }

   public MemberField(AnnotationList annotations, ModifierList list, TextLiteral identifier, Constraint constraint) {
      this(annotations, list, identifier, constraint, null);
   }

   public MemberField(AnnotationList annotations, ModifierList list, TextLiteral identifier, Evaluation value) {
      this(annotations, list, identifier, null, value);
   }

   public MemberField(AnnotationList annotations, ModifierList list, TextLiteral identifier, Constraint constraint, Evaluation value) {
      this.declaration = new MemberFieldDeclaration(list, identifier, constraint, value);
      this.extractor = new ConstraintExtractor(constraint);
      this.checker = new ModifierChecker(list);
      this.annotations = annotations;
      this.identifier = identifier;
      this.list = list;
   }

   @Override
   public Initializer compile(Scope scope, Initializer initializer, Type type) throws Exception {
      Initializer declare = declaration.declare(scope, initializer);
      List<Property> properties = type.getProperties();
      Value value = identifier.evaluate(scope, null);
      Type constraint = extractor.extract(scope);
      String name = value.getString();
      int modifiers = list.getModifiers();
      
      if (checker.isStatic()) {
         Accessor accessor = new StaticAccessor(initializer, scope, type, name);
         Property property = new AccessorProperty(name, type, constraint, accessor, modifiers);
         
         annotations.apply(scope, property);
         properties.add(property);
      } else {
         Accessor accessor = new ScopeAccessor(name);
         Property property = new AccessorProperty(name, type, constraint, accessor, modifiers); // is this the correct type!!??
         
         annotations.apply(scope, property);
         properties.add(property);
      }
      return declare;
   }
}
