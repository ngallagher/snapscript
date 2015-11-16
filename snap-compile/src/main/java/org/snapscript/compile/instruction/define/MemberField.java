package org.snapscript.compile.instruction.define;

import org.snapscript.compile.instruction.Constraint;
import org.snapscript.compile.instruction.DeclareConstant;
import org.snapscript.compile.instruction.DeclareVariable;
import org.snapscript.compile.instruction.Evaluation;
import org.snapscript.compile.instruction.TextLiteral;
import org.snapscript.core.Initializer;
import org.snapscript.core.ModifierType;
import org.snapscript.core.Property;
import org.snapscript.core.Scope;
import org.snapscript.core.ScopeAccessor;
import org.snapscript.core.StaticAccessor;
import org.snapscript.core.Type;
import org.snapscript.core.Value;

public class MemberField implements TypePart {
   
   private final TextLiteral identifier;
   private final Constraint constraint;
   private final Evaluation value;  
   private final ModifierList modifier;
   
   public MemberField(ModifierList modifier, TextLiteral identifier) {
      this(modifier, identifier, null, null);
   }
   
   public MemberField(ModifierList modifier, TextLiteral identifier, Constraint constraint) {      
      this(modifier, identifier, constraint, null);
   }
   
   public MemberField(ModifierList modifier, TextLiteral identifier, Evaluation value) {
      this(modifier, identifier, null, value);
   }
   
   public MemberField(ModifierList modifier, TextLiteral identifier, Constraint constraint, Evaluation value) {
      this.constraint = constraint;
      this.identifier = identifier;
      this.modifier = modifier;
      this.value = value;
   }

   @Override
   public Initializer define(Scope scope, Initializer statements, Type type) throws Exception { // declare variable
      //DeclarationStatement s = new DeclarationStatement(identifier, constraint, value);
      Value vvvv = identifier.evaluate(scope, null);
      Value mod = modifier.evaluate(scope, null);
      int modifiers = mod.getInteger();
      
      if(ModifierType.isStatic(modifiers)) {
         //DeclarationStatement s = new DeclarationStatement(identifier, constraint, value);
         String name = vvvv.getString();
         String qualifier = type.getName();
         Initializer st =null;
         if(ModifierType.isConstant(modifiers)) {
            Evaluation e= new DeclareConstant(identifier, constraint, value);
           st= new StaticInitializer(e, scope);
         } else {
            Evaluation e= new DeclareVariable(identifier, constraint, value);
            st= new StaticInitializer(e, scope);
         }
         StaticAccessor accessor = new StaticAccessor(statements,scope,type, name); 
         Property property = new Property(name, type, accessor);
         
         // XXX add properties!!!
         type.getProperties().add(property);
         // XXX property needs to go in to the definition of the type...
         //statement.execute(scope);
         return st;
      }
      String name = vvvv.getString();
      ScopeAccessor accessor = new ScopeAccessor(name);
      Property property = new Property(name, type, accessor);
      
      // XXX add properties!!!
      type.getProperties().add(property);
      // XXX property needs to go in to the definition of the type...
      //statement.execute(scope);
      if(ModifierType.isConstant(modifiers)) {
         Evaluation e= new DeclareConstant(identifier, constraint, value);
         return new InstanceInitializer(e, type);
      }
      Evaluation e= new DeclareVariable(identifier, constraint, value);
      return new InstanceInitializer(e, type);
      
   }
}