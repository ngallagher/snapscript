package org.snapscript.compile.instruction.define;

import org.snapscript.core.Function;
import org.snapscript.core.Initializer;
import org.snapscript.core.Invocation;
import org.snapscript.core.Scope;
import org.snapscript.core.Signature;
import org.snapscript.core.Statement;
import org.snapscript.core.Type;

public class MemberFunctionBuilder implements TypeFunctionBuilder {
      
   private final Statement statement;
   private final Signature signature;
   private final String name;
   private final int modifiers;

   public MemberFunctionBuilder(Signature signature, Statement statement, String name, int modifiers) {
      this.signature = signature;
      this.statement = statement;
      this.modifiers = modifiers;
      this.name = name;
   }
   
   @Override
   public Function create(Scope scope, Initializer initializer, Type type){
      Invocation invocation = new InstanceInvocation(statement, signature);
      return new Function(signature, invocation, type, name, modifiers);
   }
}
