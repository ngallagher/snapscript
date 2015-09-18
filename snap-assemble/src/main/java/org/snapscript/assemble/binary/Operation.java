package org.snapscript.assemble.binary;

import org.snapscript.assemble.Instruction;

public class Operation {

   public final Instruction instruction;
   public final OperationCode code;
   public final Integer count;
   public final Object value;
   
   public Operation(OperationCode code, Instruction instruction, Integer count){
      this(code, instruction, count, null);
   }
   
   public Operation(OperationCode code, Instruction instruction, Integer count, Object value){
      this.instruction = instruction;
      this.count = count;
      this.value = value;
      this.code = code;
      
   }
}
