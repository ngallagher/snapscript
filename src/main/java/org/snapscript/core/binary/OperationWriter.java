package org.snapscript.core.binary;

import java.io.DataOutputStream;
import java.io.OutputStream;

import org.snapscript.core.execute.Instruction;

public class OperationWriter {
   
   private final DataOutputStream file;
   private final BinaryEncoder encoder;
   
   public OperationWriter(OutputStream file) {
      this.file = new DataOutputStream(file);
      this.encoder = new BinaryEncoder();      
   }
   
   public void write(Operation operation) throws Exception {//[8-bits]|[8-bits]|[8-bits]|[8-bits] -> [operation-code][instruction-code]|[argument-count]|[value-true-or-false]
      Instruction instruction = operation.instruction;
      int code = operation.code.code;
      
      file.writeChar(code);
      
      if(instruction != null){
         int index = instruction.ordinal();
         
         file.writeBoolean(true);
         file.writeInt(index);
      } else {
         file.writeBoolean(false);
      }
      file.writeInt(operation.count);
      encoder.write(file, operation.value);
   }
   
   public void close() throws Exception{
      file.flush();
      file.close();
   }
}
