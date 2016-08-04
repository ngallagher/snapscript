package org.snapscript.compile.define;

import java.util.List;

import org.snapscript.compile.ModifierChecker;
import org.snapscript.compile.ModifierList;
import org.snapscript.compile.annotation.AnnotationList;
import org.snapscript.compile.constraint.Constraint;
import org.snapscript.compile.constraint.ConstraintExtractor;
import org.snapscript.compile.literal.TextLiteral;
import org.snapscript.core.Evaluation;
import org.snapscript.core.InternalStateException;
import org.snapscript.core.ModifierType;
import org.snapscript.core.Module;
import org.snapscript.core.Scope;
import org.snapscript.core.Type;
import org.snapscript.core.Value;
import org.snapscript.core.define.Initializer;
import org.snapscript.core.function.Accessor;
import org.snapscript.core.function.AccessorProperty;
import org.snapscript.core.function.StaticAccessor;
import org.snapscript.core.property.Property;

public class TraitConstant implements TypePart {

   private final TraitConstantDeclaration declaration;
   private final ConstraintExtractor extractor;
   private final AnnotationList annotations;
   private final ModifierChecker checker;
   private final TextLiteral identifier;
   
   public TraitConstant(AnnotationList annotations, ModifierList list, TextLiteral identifier, Evaluation value) {
      this(annotations, list, identifier, null, value);
   }
   
   public TraitConstant(AnnotationList annotations, ModifierList list, TextLiteral identifier, Constraint constraint, Evaluation value) {
      this.declaration = new TraitConstantDeclaration(identifier, constraint, value);
      this.extractor = new ConstraintExtractor(constraint);
      this.checker = new ModifierChecker(list);
      this.annotations = annotations;
      this.identifier = identifier;
   }

   @Override
   public Initializer compile(Initializer initializer, Type type) throws Exception {
      Scope scope = type.getScope();
      Module module = type.getModule();
      Initializer declare = declaration.declare(initializer);
      List<Property> properties = type.getProperties();
      Value value = identifier.evaluate(scope, null);
      Type constraint = extractor.extract(scope);
      String name = value.getString();
      
      if(!checker.isConstant()) {
         throw new InternalStateException("Variable '" + name + "' for " +module + "." + type + " must be constant");
      }
      Accessor accessor = new StaticAccessor(initializer, scope, type, name);
      Property property = new AccessorProperty(name, type, constraint, accessor, ModifierType.STATIC.mask | ModifierType.CONSTANT.mask);
      
      annotations.apply(scope, property);
      properties.add(property);

      return declare;
   }
}