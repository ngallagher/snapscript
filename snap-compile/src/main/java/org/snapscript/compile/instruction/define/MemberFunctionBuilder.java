package org.snapscript.compile.instruction.define;

import org.snapscript.compile.instruction.CompoundStatement;
import org.snapscript.core.Function;
import org.snapscript.core.Initializer;
import org.snapscript.core.Invocation;
import org.snapscript.core.Scope;
import org.snapscript.core.Signature;
import org.snapscript.core.Statement;
import org.snapscript.core.Type;

public class MemberFunctionBuilder {

   private final Statement statement;

   public MemberFunctionBuilder(Statement statement) {
      this.statement = statement;
   }
   
   public Function create(Signature signature, Initializer initializer, Scope scope, Type type, String name) throws Exception {
      Statement init = new InitializerStatement(initializer, type); // initialize static scope first
      Statement compound = new CompoundStatement(init, statement); // this should call onlt the init stuff
      Invocation invocation = new StaticInvocation(compound, signature, scope);
      
      return new Function(signature, invocation, name);// description is wrong here..... 
   }
   
   public Function create(Signature signature, Scope scope, Type type, String name) throws Exception {
      Invocation invocation = new InstanceInvocation(statement, signature);
      //Invocation scopeCall = new TypeInvocation(invocation, scope); // ensure the static stuff is in scope
      return new Function(signature, invocation, name);// description is wrong here.....
   }
}
