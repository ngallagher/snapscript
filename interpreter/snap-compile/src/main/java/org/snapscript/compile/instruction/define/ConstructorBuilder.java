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
   
   private final Initializer delegate;
   private final Statement statement;
   private final Signature signature;

   public ConstructorBuilder(Signature signature, Statement statement, Initializer delegate) {
      this.signature = signature;
      this.statement = statement;
      this.delegate = delegate;
   }
   
   public Function create(Scope scope, Initializer initializer, Type type) {
      return create(scope, initializer, type);
   }
   
   public Function create(Scope scope, Initializer initializer, Type type, boolean enumeration) {
      Invocation body = new StatementInvocation(statement, signature);
      Invocation construct = new ConstructInvocation(scope, initializer, body, enumeration);
      Invocation invocation = new NewInvocation(signature, delegate, construct); 
      Invocation reference = new TypeInvocation(invocation, scope);
      
      return new Function(signature, reference, TYPE_CONSTRUCTOR, 1);
   }
}