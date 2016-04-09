package org.snapscript.compile.instruction.define;

import static org.snapscript.core.Reserved.TYPE_CLASS;

import org.snapscript.compile.instruction.ParameterList;
import org.snapscript.core.Initializer;
import org.snapscript.core.Scope;
import org.snapscript.core.Signature;
import org.snapscript.core.Statement;
import org.snapscript.core.Type;

public class ConstructorAssembler {

   private final DelegateInitializer delegate; // this() or super()
   private final ParameterList parameters;
   private final Statement body;

   public ConstructorAssembler(ParameterList parameters, TypePart part, Statement body){  
      this.delegate = new DelegateInitializer(part);
      this.parameters = parameters;
      this.body = body;
   } 
   
   public ConstructorBuilder assemble(Scope scope, Initializer initializer, Type type) throws Exception {
      Initializer factory = delegate.compile(scope, initializer, type);
      Signature signature = parameters.create(scope, TYPE_CLASS);
      
      return new ConstructorBuilder(signature, body, factory);
   }
}
