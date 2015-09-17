package org.snapscript.assembler;

public interface Instruction {
   String getName();
   Class getType();
   int getCode();
}
