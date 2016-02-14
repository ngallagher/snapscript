package org.snapscript.compile.instruction.define;

import static org.snapscript.core.Reserved.TYPE_CLASS;

import org.snapscript.compile.instruction.ParameterList;
import org.snapscript.core.Initializer;
import org.snapscript.core.Scope;
import org.snapscript.core.Signature;
import org.snapscript.core.Statement;
import org.snapscript.core.Type;

public class ConstructorAssembler {

   private final DelegateInitializer initializer; // this() or super()
   private final ParameterList parameters;
   private final Statement statement;

   public ConstructorAssembler(ParameterList parameters, TypePart part, Statement statement){  
      this.initializer = new DelegateInitializer(part);
      this.parameters = parameters;
      this.statement = statement;
   } 
   
   public ConstructorBuilder assemble(Scope scope, Type type) throws Exception {
      Initializer base = initializer.define(scope, type);
      Signature signature = parameters.create(scope, TYPE_CLASS);
      
      return new ConstructorBuilder(signature, statement, base);
   }
}
