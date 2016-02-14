package org.snapscript.compile.instruction.define;

import org.snapscript.compile.instruction.CompoundStatement;
import org.snapscript.core.Function;
import org.snapscript.core.Initializer;
import org.snapscript.core.Invocation;
import org.snapscript.core.Scope;
import org.snapscript.core.Signature;
import org.snapscript.core.Statement;
import org.snapscript.core.Type;

public class StaticFunctionBuilder implements TypeFunctionBuilder {
   
   private final Signature signature;
   private final Statement body;
   private final String name;
   private final int modifiers;

   public StaticFunctionBuilder(Signature signature, Statement body, String name, int modifiers) {
      this.signature = signature;
      this.modifiers = modifiers;
      this.body = body;
      this.name = name;
   }
   
   @Override
   public Function create(Scope scope, Initializer initializer, Type type){
      Statement initialize = new InitializerStatement(initializer, type); 
      Statement statement = new CompoundStatement(initialize, body); 
      Invocation invocation = new StaticInvocation(statement, signature, scope);
      
      return new Function(signature, invocation, name, modifiers);
   }
}