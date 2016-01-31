package org.snapscript.compile.instruction.define;

import static org.snapscript.core.Reserved.TYPE_CONSTRUCTOR;

import org.snapscript.compile.instruction.StatementInvocation;
import org.snapscript.core.Function;
import org.snapscript.core.Initializer;
import org.snapscript.core.Invocation;
import org.snapscript.core.Scope;
import org.snapscript.core.Signature;
import org.snapscript.core.Statement;
import org.snapscript.core.Type;

public class MemberConstructorBuilder {

   private final Statement statement;

   public MemberConstructorBuilder(Statement statement){  
      this.statement = statement;
   } 
   
   public Function create(Signature signature, Scope scope, Initializer statements, Initializer baseCall, Type type, boolean enumeration) throws Exception {
      Invocation bodyCall = new StatementInvocation(statement, signature);
      Invocation invocation = new NewInvocation(scope, signature, baseCall, statements, bodyCall, enumeration); 
      Invocation scopeCall = new BoundaryInvocation(invocation, scope);
      
      return new Function(signature, scopeCall, TYPE_CONSTRUCTOR, 1);
   }
}
