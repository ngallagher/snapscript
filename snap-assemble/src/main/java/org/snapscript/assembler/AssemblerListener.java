package org.snapscript.assembler;

import org.snapscript.core.binary.OperationCode;

public interface AssemblerListener {
   void assemble(String name, OperationCode code, Instruction instruction, Integer count, Object value);
}
