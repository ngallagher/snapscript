package org.snapscript.compile.instruction.define;

import java.util.List;

import org.snapscript.compile.instruction.Constraint;
import org.snapscript.compile.instruction.DeclareConstant;
import org.snapscript.compile.instruction.DeclareVariable;
import org.snapscript.compile.instruction.Evaluation;
import org.snapscript.compile.instruction.TextLiteral;
import org.snapscript.core.Accessor;
import org.snapscript.core.Bug;
import org.snapscript.core.Initializer;
import org.snapscript.core.Property;
import org.snapscript.core.Scope;
import org.snapscript.core.ScopeAccessor;
import org.snapscript.core.StaticAccessor;
import org.snapscript.core.Type;
import org.snapscript.core.Value;

@Bug("This should be used for ModifierField")
public class MemberFieldBuilder {
   
   private final ModifierChecker checker;
   private final TextLiteral identifier;
   private final Constraint constraint;
   private final Evaluation value;

   public MemberFieldBuilder(ModifierList modifiers, TextLiteral identifier) {
      this(modifiers, identifier, null, null);
   }

   public MemberFieldBuilder(ModifierList modifiers, TextLiteral identifier, Constraint constraint) {
      this(modifiers, identifier, constraint, null);
   }

   public MemberFieldBuilder(ModifierList modifiers, TextLiteral identifier, Evaluation value) {
      this(modifiers, identifier, null, value);
   }

   public MemberFieldBuilder(ModifierList modifiers, TextLiteral identifier, Constraint constraint, Evaluation value) {
      this.checker = new ModifierChecker(modifiers);
      this.constraint = constraint;
      this.identifier = identifier;
      this.value = value;
   }
   
   public Initializer create(Scope scope, Initializer statements, Type type) throws Exception {
      Value value = identifier.evaluate(scope, null);
      String name = value.getString();
      List<Property> properties = type.getProperties();
      Property property = createProperty(scope, statements, type, name);
      Initializer initializer = createInitializer(scope, type);
      
      properties.add(property);
      
      return initializer;
   }
   
   private Property createProperty(Scope scope, Initializer statements, Type type, String name) throws Exception {
      Accessor accessor = createAccessor(scope, statements, type);
      
      return new Property(name, type, accessor); // is this the correct type!!??
   }
   
   private Accessor createAccessor(Scope scope, Initializer statements, Type type) throws Exception {
      Value value = identifier.evaluate(scope, null);
      String name = value.getString();

      if (checker.isStatic()) {
         return new StaticAccessor(statements, scope, type, name);
      }
      return new ScopeAccessor(name);
   }

   private Initializer createInitializer(Scope scope, Type type) throws Exception {
      Evaluation evaluation = createDeclaration(scope, type);

      if (checker.isStatic()) {
         return new StaticInitializer(evaluation, scope);
      }
      return new InstanceInitializer(evaluation, type);
   }
 
   private Evaluation createDeclaration(Scope scope, Type type) throws Exception {
      if (checker.isConstant()) {
         return new DeclareConstant(identifier, constraint, value);
      }
      return new DeclareVariable(identifier, constraint, value);
   }
}