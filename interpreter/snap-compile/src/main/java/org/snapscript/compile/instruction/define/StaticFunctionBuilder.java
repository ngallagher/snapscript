package org.snapscript.compile.instruction.define;

import org.snapscript.compile.instruction.CompoundStatement;
import org.snapscript.core.Bug;
import org.snapscript.core.Function;
import org.snapscript.core.Initializer;
import org.snapscript.core.Invocation;
import org.snapscript.core.Scope;
import org.snapscript.core.Signature;
import org.snapscript.core.Statement;
import org.snapscript.core.Type;

public class StaticFunctionBuilder implements TypeFunctionBuilder {
   
   private final Statement statement;
   private final Signature signature;
   private final String name;
   private final int modifiers;

   public StaticFunctionBuilder(Signature signature, Statement statement, String name, int modifiers) {
      this.signature = signature;
      this.statement = statement;
      this.modifiers = modifiers;
      this.name = name;
   }
   
   @Bug
   @Override
   public Function create(Scope scope, Initializer initializer, Type type){
      Statement init = new InitializerStatement(initializer, type); // initialize static scope first
      Statement compound = new CompoundStatement(init, statement); // this should call onlt the init stuff
      Invocation invocation = new StaticInvocation(compound, signature, scope);
      
      return new Function(signature, invocation, name, modifiers);// description is wrong here..... 
   }
}