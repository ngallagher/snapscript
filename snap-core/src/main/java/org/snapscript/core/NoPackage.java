package org.snapscript.core;

public class NoPackage implements Package {

   @Override
   public Statement compile(Scope scope) throws Exception {
      return new NoStatement();
   }
}
