package org.snapscript.core;

public interface Scope {
   Type getType();
   Scope getScope();
   Module getModule();
   Context getContext();    
   Value getValue(String name);
   void setValue(String name, Value value);
   void addVariable(String name, Reference value);
   void addConstant(String name, Constant value);
}
