package org.snapscript.compile.instruction;

import java.util.HashMap;
import java.util.Map;

import org.snapscript.core.Context;
import org.snapscript.core.Type;
import org.snapscript.core.TypeLoader;

public class OperationResolver {
   
   private final Map<String, Operation> operations;
   private final Context context;

   public OperationResolver(Context context) {
      this.operations = new HashMap<String, Operation>();
      this.context = context;
   }

   public Operation resolve(String name) throws Exception {
      if(operations.isEmpty()) {
         Instruction[] list = Instruction.values();       
      
         for(Instruction instruction :list){
            Operation operation = create(instruction);
            String grammar = instruction.getName();
            
            operations.put(grammar, operation);
         }  
      } 
      return operations.get(name);
   }
   
   private Operation create(Instruction instruction) throws Exception{
      TypeLoader loader = context.getLoader();
      Class value = instruction.getType();
      Type type = loader.loadType(value);
      
      return new Operation(instruction, type);
   }
}
