package org.snapscript.compile.instruction;

import java.util.ArrayList;
import java.util.List;

import org.snapscript.core.Context;
import org.snapscript.core.Module;
import org.snapscript.core.Scope;
import org.snapscript.core.Signature;
import org.snapscript.core.Type;
import org.snapscript.core.TypeLoader;
import org.snapscript.core.Value;

public class ParameterList {
   
   private VariableArgumentChecker checker;
   private ParameterDeclaration[] list;
   private Signature signature;
   
   public ParameterList(ParameterDeclaration... list) {
      this.checker = new VariableArgumentChecker(list);
      this.list = list;
   }
   
   public Signature create(Scope scope) throws Exception{
      return create(scope, null);
   }
   
   public Signature create(Scope scope, String prefix) throws Exception{
      if(signature == null) {
         List<String> names = new ArrayList<String>();
         List<Type> constraints = new ArrayList<Type>();
         
         if(prefix != null) {
            Module module = scope.getModule();
            Context context = module.getContext();
            TypeLoader loader = context.getLoader();
            Type constraint = loader.loadType(Type.class);
            
            names.add(prefix);
            constraints.add(constraint);
         }
         boolean variable = checker.isVariable(scope);
         
         for(int i = 0; i < list.length; i++) {
            Parameter parameter = list[i].get(scope);
            Type type = parameter.getType();
            String name = parameter.getName();

            if(type != null) {
               constraints.add(type);
            } else {
               constraints.add(null);
            }
            names.add(name);
         }
         signature = new Signature(names, constraints, variable);
      }
      return signature;
   }
}