package org.snapscript.compile.instruction.define;

import static org.snapscript.core.ModifierType.STATIC;
import static org.snapscript.core.Reserved.TYPE_CONSTRUCTOR;

import org.snapscript.compile.instruction.StatementInvocation;
import org.snapscript.core.Function;
import org.snapscript.core.Initializer;
import org.snapscript.core.Invocation;
import org.snapscript.core.InvocationFunction;
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
   
   public Function create(Scope scope, Initializer initializer, Type type, int modifiers) {
      return create(scope, initializer, type, modifiers);
   }
   
   public Function create(Scope scope, Initializer initializer, Type type, int modifiers, boolean compile) {
      Invocation body = new StatementInvocation(signature, statement);
      Invocation construct = new ConstructInvocation(scope, initializer, body, compile);
      Invocation invocation = new NewInvocation(signature, delegate, construct); 
      Invocation reference = new TypeInvocation(invocation, scope);
      
      return new InvocationFunction(signature, reference, type, TYPE_CONSTRUCTOR, modifiers | STATIC.mask, 1);
   }
}