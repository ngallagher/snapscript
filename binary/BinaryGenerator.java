package com.zuooh.script.core.binary;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.zuooh.script.core.execute.AssemblerListener;
import com.zuooh.script.core.execute.Instruction;

public class BinaryGenerator implements AssemblerListener{
   
   private final Map<String, BinaryFile> files;
   private final String extension;

   public BinaryGenerator(String extension) {
      this.files = new LinkedHashMap<String, BinaryFile>();
      this.extension = extension;
   }

   @Override
   public void assemble(String name, OperationCode code, Instruction instruction, Integer count, Object value){
      BinaryFile file = files.get(name);
      
      if(file == null) {
         file = new BinaryFile(name);
         files.put(name, file);
      }
      try {
         file.collect(code, instruction, count, value);
      } catch(Exception e){
         throw new IllegalStateException("Error collecting operation for " + name, e);
      }
   }

   private class BinaryFile {
      
      private final List<Operation> operations;
      private final String name;
      
      public BinaryFile(String name){
         this.operations = new ArrayList<Operation>();
         this.name = name;
      }
      
      public void collect(OperationCode code, Instruction instruction, Integer count, Object value) throws Exception {
         Operation operation = new Operation(code, instruction, count, value);
         
         if(operation.code==OperationCode.END){
            FileOutputStream file = new FileOutputStream(name + "." + extension);
            BufferedOutputStream buffer = new BufferedOutputStream(file);
            OperationWriter writer = new OperationWriter(buffer);
            Operation begin = new Operation(OperationCode.BEGIN, Instruction.SCRIPT, 0, null);
            Operation version = new Operation(OperationCode.VERSION, Instruction.SCRIPT, 0, null);            

            writer.write(begin);
            writer.write(version);
            
            for(Operation op : operations){              
               writer.write(op);               
            }
            writer.write(operation);
            writer.close();
         } else {
            operations.add(operation);
         }
      }
   }
}
