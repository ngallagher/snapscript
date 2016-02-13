package org.snapscript.compile.instruction.define;

import static org.snapscript.core.Reserved.TYPE_CLASS;
import static org.snapscript.core.Reserved.TYPE_CONSTRUCTOR;

import org.snapscript.compile.instruction.ParameterList;
import org.snapscript.compile.instruction.StatementInvocation;
import org.snapscript.core.Bug;
import org.snapscript.core.Function;
import org.snapscript.core.Initializer;
import org.snapscript.core.Invocation;
import org.snapscript.core.Scope;
import org.snapscript.core.Signature;
import org.snapscript.core.Statement;
import org.snapscript.core.Type;

public class MemberConstructorBuilder {

   private final BaseInitializer initializer;
   private final ParameterList parameters;
   private final Statement statement;

   public MemberConstructorBuilder(ParameterList parameters, TypePart part, Statement statement){  
      this.initializer = new BaseInitializer(part);
      this.parameters = parameters;
      this.statement = statement;
   } 
   
   @Bug("clean up")
   public Function create(Scope scope, Initializer statements, Type type, boolean enumeration) throws Exception {
      Initializer base = initializer.define(scope, type);
      Signature signature = parameters.create(scope, TYPE_CLASS);
      
      Invocation body = new StatementInvocation(statement, signature);
      Invocation construct = new ConstructInvocation(scope, statements, body, enumeration);
      Invocation invocation = new NewInvocation(signature, base, construct); 
      Invocation scopeCall = new BoundaryInvocation(invocation, scope);
      
      return new Function(signature, scopeCall, TYPE_CONSTRUCTOR, 1);
   }
}
