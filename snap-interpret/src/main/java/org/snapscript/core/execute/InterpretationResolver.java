package org.snapscript.core.execute;

import java.util.Arrays;
import java.util.List;

import org.snapscript.assemble.Instruction;
import org.snapscript.assemble.InstructionResolver;

public class InterpretationResolver implements InstructionResolver {

   @Override
   public List<Instruction> list() {
      Instruction[] list = Interpretation.values();
      return Arrays.asList(list);
   }
}
