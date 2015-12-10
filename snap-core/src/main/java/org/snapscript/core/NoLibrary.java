package org.snapscript.core;

public class NoLibrary implements Package {

   @Override
   public Statement compile(Scope scope) throws Exception {
      return new NoStatement();
   }
}
