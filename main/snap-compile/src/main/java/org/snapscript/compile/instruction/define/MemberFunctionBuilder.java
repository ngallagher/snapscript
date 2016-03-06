package org.snapscript.compile.instruction.define;

import org.snapscript.core.Function;
import org.snapscript.core.Initializer;
import org.snapscript.core.InternalStateException;
import org.snapscript.core.Invocation;
import org.snapscript.core.ModifierType;
import org.snapscript.core.Scope;
import org.snapscript.core.Signature;
import org.snapscript.core.Statement;
import org.snapscript.core.Type;

public class MemberFunctionBuilder implements TypeFunctionBuilder {
      
   private final Signature signature;
   private final Statement body;
   private final String name;
   private final int modifiers;

   public MemberFunctionBuilder(Signature signature, Statement body, String name, int modifiers) {
      this.signature = signature;
      this.body = body;
      this.modifiers = modifiers;
      this.name = name;
   }
   
   @Override
   public Function create(Scope scope, Initializer initializer, Type type){
      Invocation invocation = new InstanceInvocation(signature, body);
      Function function = new Function(signature, invocation, type, name, modifiers);
      
      if(!ModifierType.isAbstract(modifiers)) {
         if(body == null) {
            throw new InternalStateException("Function '" + function + "' is not abstract");
         }
      }
      return function;
   }
}
