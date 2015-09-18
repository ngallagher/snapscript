package org.snapscript.assemble;

public interface Instruction {
   String getName();
   Class getType();
   int getCode();
}
