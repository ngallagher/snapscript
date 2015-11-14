package org.snapscript.core;

public interface State {
   Value getValue(String name);
   void setValue(String name, Value value);
   void addVariable(String name, Value value);
   void addConstant(String name, Value value);
}
