package org.snapscript.core.export;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.snapscript.core.Bug;
import org.snapscript.core.Context;
import org.snapscript.core.Function;
import org.snapscript.core.Invocation;
import org.snapscript.core.InvocationFunction;
import org.snapscript.core.Parameter;
import org.snapscript.core.ParameterBuilder;
import org.snapscript.core.Result;
import org.snapscript.core.Scope;
import org.snapscript.core.Signature;
import org.snapscript.core.Type;
import org.snapscript.core.TypeLoader;

public class MethodExporter {
   
   private final ParameterBuilder builder;
   private final Context context;
   
   public MethodExporter(Context context){
      this.builder = new ParameterBuilder();
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

   @Bug
   private Function export(Object value, Function function) {
      String name = function.getName();
      Invocation invocation = function.getInvocation();
      Signature signature = function.getSignature();
      List<Parameter> parameters = signature.getParameters();
      
      if(!parameters.isEmpty()) {
         Parameter parameter = parameters.get(0);
         Type start = parameter.getType();
         Class type = start.getType();
         Type constraint = function.getConstraint();
         boolean variable = signature.isVariable();
         int modifiers = function.getModifiers();
         int length = parameters.size();
      
         if(type == Scope.class) {
            List<Parameter> copy = new ArrayList<Parameter>();
            Signature reduced = new Signature(copy, variable);
            Invocation adapter = new ExportInvocation(invocation, value);
         
            for(int i = 1; i < length; i++) {
               Parameter next = parameters.get(i);
               Type t = next.getType();
               Parameter build = builder.create(t, i - 1);
               copy.add(build);
            }
            return new InvocationFunction(reduced, adapter, null, constraint, name, modifiers);
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
