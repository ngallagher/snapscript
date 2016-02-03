package org.snapscript.core.export;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.snapscript.core.Context;
import org.snapscript.core.Function;
import org.snapscript.core.Invocation;
import org.snapscript.core.Result;
import org.snapscript.core.Scope;
import org.snapscript.core.Signature;
import org.snapscript.core.Type;
import org.snapscript.core.TypeLoader;

public class MethodExporter {

   private final Context context;
   
   public MethodExporter(Context context){
      this.context = context;
   }

   public List<Function> export(Object value) throws Exception {
      Class require = value.getClass();
      TypeLoader loader = context.getLoader();
      Type type = loader.loadType(require);
      List<Function> functions = type.getFunctions();
      
      if(!functions.isEmpty()) {
         List<Function> adapters = new ArrayList<Function>();
         
         for(Function function : functions) {
            Function adapter = export(value, function);
            
            if(adapter != null) {
               adapters.add(adapter);
            }
         }
         return adapters;
      }
      return Collections.emptyList();
   }

   private Function export(Object value, Function function) {
      String name = function.getName();
      Invocation invocation = function.getInvocation();
      Signature signature = function.getSignature();
      List<Type> parameters = signature.getTypes();
      
      if(!parameters.isEmpty()) {
         Type start = parameters.get(0); 
         Class type = start.getType();
         boolean variable = signature.isVariable();
         int modifiers = function.getModifiers();
         int length = parameters.size();
      
         if(type == Scope.class) {
            List<String> names = new ArrayList<String>();
            List<Type> types = new ArrayList<Type>();
            Signature reduced = new Signature(names, types, variable);
            Invocation adapter = new ExportInvocation(invocation, value);
         
            for(int i = 1; i < length; i++) {
               Type parameter = parameters.get(i);
               
               names.add("a" + 1);
               types.add(parameter);
            }
            return new Function(reduced, adapter, name, modifiers);
         }
      }
      return null;
   }
   
   private static class ExportInvocation implements Invocation<Object>{

      private final Invocation invocation;
      private final Object target;
      
      public ExportInvocation(Invocation invocation, Object target) {
         this.invocation = invocation;
         this.target = target;
      }
      
      @Override
      public Result invoke(Scope scope, Object left, Object... list) throws Exception {
         Object[] arguments = new Object[list.length + 1];
         
         for(int i = 0; i < list.length; i++) {
            arguments[i + 1] = list[i];
         }
         arguments[0] = scope;
         
         return invocation.invoke(scope, target, arguments);
      }
   }
}
