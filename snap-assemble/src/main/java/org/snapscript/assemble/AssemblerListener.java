package org.snapscript.assemble;

import org.snapscript.assemble.binary.OperationCode;

public interface AssemblerListener {
   void assemble(String name, OperationCode code, Instruction instruction, Integer count, Object value);
}
