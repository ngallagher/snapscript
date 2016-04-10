package org.snapscript.core.export;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.snapscript.core.Context;
import org.snapscript.core.Function;
import org.snapscript.core.Invocation;
import org.snapscript.core.InvocationFunction;
import org.snapscript.core.Result;
import org.snapscript.core.Scope;
import org.snapscript.core.Signature;
import org.snapscript.core.Type;
import org.snapscript.core.TypeLoader;

public class MethodExporter {
   
   private static final String[] PREFIX = {
   "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", 
   "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z" };

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
         Type constraint = function.getConstraint();
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
               String prefix = PREFIX[i % PREFIX.length];
               
               if(i > PREFIX.length) {
                  prefix += i / PREFIX.length;
               }
               names.add(prefix);
               types.add(parameter);
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
