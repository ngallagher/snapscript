package org.snapscript.interpret;

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
   
   private Signature signature;
   private Parameter[] list;
   
   public ParameterList(Parameter... list) {
      this.list = list;
   }
   
   public Signature create(Scope scope) throws Exception{
      return create(scope, null);
   }
   
   public Signature create(Scope scope, String prefix) throws Exception{
      if(signature == null) {
         List<String> names = new ArrayList<String>();
         List<Type> constraints = new ArrayList<Type>();
         Signature result = new Signature(names, constraints, 0);
         
         if(prefix != null) {
            Module module = scope.getModule();
            Context context = module.getContext();
            TypeLoader loader = context.getLoader();
            Type constraint = loader.load(Type.class);
            
            names.add(prefix);
            constraints.add(constraint);
         }
         for(int i = 0; i < list.length; i++) {
            Value value = list[i].evaluate(scope, null);
            String constraint = value.getConstraint();
            String name = value.getString();
            
            if(constraint != null) {
               Module module = scope.getModule();
               Type type = module.getType(constraint);
               
               constraints.add(type);
            } else {
               constraints.add(null);
            }
            names.add(name);
         }
         return signature = result;
      }
      return signature;
   }
}