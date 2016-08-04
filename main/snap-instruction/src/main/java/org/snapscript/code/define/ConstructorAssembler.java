package org.snapscript.code.define;

import static org.snapscript.core.Reserved.TYPE_CLASS;

import org.snapscript.code.function.ParameterList;
import org.snapscript.core.Scope;
import org.snapscript.core.Statement;
import org.snapscript.core.Type;
import org.snapscript.core.define.Initializer;
import org.snapscript.core.function.Signature;

public class ConstructorAssembler {

   private final DelegateInitializer delegate; // this() or super()
   private final ParameterList parameters;
   private final Statement body;

   public ConstructorAssembler(ParameterList parameters, TypePart part, Statement body){  
      this.delegate = new DelegateInitializer(part);
      this.parameters = parameters;
      this.body = body;
   } 
   
   public ConstructorBuilder assemble(Initializer initializer, Type type) throws Exception {
      Scope scope = type.getScope();
      Initializer factory = delegate.compile(initializer, type);
      Signature signature = parameters.create(scope, TYPE_CLASS);
      
      return new ConstructorBuilder(signature, body, factory);
   }
}
