package org.snapscript.compile.instruction;

import org.snapscript.core.Scope;

public class VariableArgumentChecker {

   private ParameterDeclaration[] list;
   
   public VariableArgumentChecker(ParameterDeclaration... list) {
      this.list = list;
   }
   
   public boolean isVariable(Scope scope) throws Exception {
      int length = list.length;
      int count = 0;
      
      for(int i = 0; i < length - 1; i++) {
         ParameterDeclaration declaration = list[i];
         
         if(declaration != null) {
            Parameter parameter = declaration.get(scope);
            String name = parameter.getName();
         
            if(parameter.isVariable()) {
               throw new IllegalStateException("Illegal declaration " + name + "... at index " + i);
            }
            count++;
         }
         
      }
      if(count > 0) {
         ParameterDeclaration declaration = list[length-1];
         Parameter parameter = declaration.get(scope);
         
         if(parameter.isVariable()) {
            return true;
         }
      }
      return false;
   }
}
