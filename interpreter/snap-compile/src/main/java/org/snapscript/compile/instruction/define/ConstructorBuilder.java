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

public class ConstructorBuilder {
   
   private final Initializer initializer;
   private final Statement statement;
   private final Signature signature;

   public ConstructorBuilder(Signature signature, Statement statement, Initializer initializer) {
      this.initializer = initializer;
      this.signature = signature;
      this.statement = statement;
   }
   
   public Function create(Scope scope, Initializer statements, Type type) {
      return create(scope, statements, type);
   }
   
   public Function create(Scope scope, Initializer statements, Type type, boolean enumeration) {
      Invocation body = new StatementInvocation(statement, signature);
      Invocation construct = new ConstructInvocation(scope, statements, body, enumeration);
      Invocation invocation = new NewInvocation(signature, initializer, construct); 
      Invocation boundary = new BoundaryInvocation(invocation, scope);
      
      return new Function(signature, boundary, TYPE_CONSTRUCTOR, 1);
   }
}