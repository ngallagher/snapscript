package com.zuooh.script.core.binary;

import java.io.DataInputStream;
import java.io.InputStream;

import com.zuooh.script.core.execute.Instruction;

public class OperationReader {
   
   private final DataInputStream file;
   private final BinaryEncoder encoder;
   
   public OperationReader(InputStream file) {
      this.file = new DataInputStream(file);
      this.encoder = new BinaryEncoder();      
   }
   
   public Operation read() throws Exception {
      Instruction instruction = null;   
      char alias = file.readChar();
      
      if(file.readBoolean()) {
         int ordinal = file.readInt();
         instruction = Instruction.resolveInstruction(ordinal);
      }
      int count = file.readInt();
      OperationCode code = OperationCode.resolveCode(alias);    
      Object value = encoder.read(file);
      
      return new Operation(code, instruction, count, value);
   }
   
   public void close() throws Exception{
      file.close();
   }
}
