package org.snapscript.compile.instruction.define;

import org.snapscript.compile.instruction.CompoundStatement;
import org.snapscript.core.Function;
import org.snapscript.core.Initializer;
import org.snapscript.core.Invocation;
import org.snapscript.core.InvocationFunction;
import org.snapscript.core.Scope;
import org.snapscript.core.Signature;
import org.snapscript.core.Statement;
import org.snapscript.core.Type;

public class StaticFunctionBuilder implements TypeFunctionBuilder {
   
   private final Signature signature;
   private final Statement body;
   private final Type constraint;
   private final String name;
   private final int modifiers;

   public StaticFunctionBuilder(Signature signature, Statement body, Type constraint, String name, int modifiers) {
      this.constraint = constraint;
      this.signature = signature;
      this.modifiers = modifiers;
      this.body = body;
      this.name = name;
   }
   
   @Override
   public Function create(Scope scope, Initializer initializer, Type type){
      Statement initialize = new StaticBody(initializer, type); 
      Statement statement = new CompoundStatement(initialize, body); 
      Invocation invocation = new StaticInvocation(signature, statement, scope);
      
      return new InvocationFunction(signature, invocation, type, constraint, name, modifiers);
   }
}