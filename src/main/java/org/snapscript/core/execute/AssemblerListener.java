package org.snapscript.core.execute;

import org.snapscript.core.binary.OperationCode;

public interface AssemblerListener {
   void assemble(String name, OperationCode code, Instruction instruction, Integer count, Object value);
}
