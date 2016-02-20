package org.snapscript.compile.instruction;

import java.util.List;

import org.snapscript.core.Scope;
import org.snapscript.core.Signature;
import org.snapscript.core.State;
import org.snapscript.core.Value;

public class ParameterExtractor {
   
   private final ParameterBuilder builder;
   private final Signature signature;
   
   public ParameterExtractor(Signature signature) {
      this.builder = new ParameterBuilder(signature);
      this.signature = signature;
   }

   public void extract(Scope scope, Object... arguments) throws Exception {
      List<String> names = signature.getNames();
      State state = scope.getState();
      
      for(int i = 0; i < arguments.length; i++) {
         try {
         String name = names.get(i);
         Object argument = arguments[i];
         Value value = builder.create(scope, argument, i);
         
         state.addVariable(name, value);
         }catch(Exception e){
            e.printStackTrace();
         }
      }
   }
}
