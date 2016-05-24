package org.snapscript.compile.instruction;

import java.util.ArrayList;
import java.util.List;

import org.snapscript.core.Bug;
import org.snapscript.core.Context;
import org.snapscript.core.Module;
import org.snapscript.core.Parameter;
import org.snapscript.core.ParameterBuilder;
import org.snapscript.core.Scope;
import org.snapscript.core.Signature;
import org.snapscript.core.Type;
import org.snapscript.core.TypeLoader;

public class ParameterList {
   
   private VariableArgumentChecker checker;
   private ParameterDeclaration[] list;
   private ParameterBuilder builder;
   private Signature signature;
   
   public ParameterList(ParameterDeclaration... list) {
      this.checker = new VariableArgumentChecker(list);
      this.builder = new ParameterBuilder();
      this.list = list;
   }
   
   public Signature create(Scope scope) throws Exception{
      return create(scope, null);
   }
   
   public Signature create(Scope scope, String prefix) throws Exception{
      if(signature == null) {
         List<Parameter> parameters = new ArrayList<Parameter>();
         
         if(prefix != null) {
            Module module = scope.getModule();
            Context context = module.getContext();
            TypeLoader loader = context.getLoader();
            Type constraint = loader.loadType(Type.class);
            Parameter parameter = builder.create(constraint, prefix);
            
            parameters.add(parameter);
         }
         boolean variable = checker.isVariable(scope);
         
         for(int i = 0; i < list.length; i++) {
            ParameterDeclaration declaration = list[i];
            
            if(declaration != null) {
               Parameter parameter = declaration.get(scope);
               parameters.add(parameter);
            }
         }
         signature = new Signature(parameters, variable);
      }
      return signature;
   }
}