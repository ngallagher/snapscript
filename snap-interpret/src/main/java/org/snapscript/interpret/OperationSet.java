package org.snapscript.interpret;

import java.util.Arrays;
import java.util.List;

import org.snapscript.assemble.Instruction;
import org.snapscript.assemble.InstructionSet;

public class OperationSet implements InstructionSet {

   @Override
   public List<Instruction> list() {
      Instruction[] list = Operation.values();
      return Arrays.asList(list);
   }
}
