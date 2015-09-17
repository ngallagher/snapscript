package org.snapscript.core.binary;

import java.io.DataInputStream;
import java.io.InputStream;
import java.util.Map;

import org.snapscript.assembler.Instruction;

public class OperationReader {
   
   private final Map<Object, Instruction> instructions;
   private final DataInputStream file;
   private final BinaryEncoder encoder;
   
   public OperationReader(Map<Object, Instruction> instructions, InputStream file) {
      this.file = new DataInputStream(file);
      this.encoder = new BinaryEncoder();   
      this.instructions = instructions;
   }
   
   public Operation read() throws Exception {
      Instruction instruction = null;   
      char alias = file.readChar();
      
      if(file.readBoolean()) {
         int ordinal = file.readInt();
         instruction = instructions.get(ordinal);
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
