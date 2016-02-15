package org.snapscript.compile.instruction;

import org.snapscript.core.Type;

public class Code {
   
   private final Instruction instruction;
   private final Type type;
   
   public Code(Instruction instruction, Type type) {
      this.instruction = instruction;
      this.type = type;
   }
   
   public Instruction getInstruction() {
      return instruction;
   }
   
   public Type getType() {
      return type;
   }
}
