package org.snapscript.core.bind;

import java.util.concurrent.Callable;

import org.snapscript.core.Module;
import org.snapscript.core.Result;
import org.snapscript.core.Scope;
import org.snapscript.core.Type;
import org.snapscript.core.TypeLoader;
import org.snapscript.core.Value;
import org.snapscript.core.convert.ConstraintMatcher;
import org.snapscript.core.error.ThreadStack;

public class FunctionBinder {
   
   private final FunctionMatcher matcher;
   
   public FunctionBinder(ConstraintMatcher matcher, TypeLoader loader, ThreadStack stack) {
      this.matcher = new FunctionMatcher(matcher, loader, stack);
   }
   
   public Callable<Result> bind(Value value, Object... list) throws Exception { // closures
      FunctionPointer call = matcher.match(value, list);
      
      if(call != null) {
         return new FunctionCall(call, null, null);
      }
      return null;
   }
   
   public Callable<Result> bind(Scope scope, String name, Object... list) throws Exception { // function variable
      FunctionPointer call = matcher.match(scope, name, list);
      
      if(call != null) {
         return new FunctionCall(call, scope, scope);
      }
      return null;
   }
   
   public Callable<Result> bind(Scope scope, Module module, String name, Object... list) throws Exception {
      FunctionPointer call = matcher.match(module, name, list);
      
      if(call != null) {
         return new FunctionCall(call, scope, module);
      }
      return null;
   }
   
   public Callable<Result> bind(Scope scope, Type type, String name, Object... list) throws Exception {
      FunctionPointer call = matcher.match(type, name, list);
      
      if(call != null) {
         return new FunctionCall(call, scope, null);
      }
      return null;
   }

   public Callable<Result> bind(Scope scope, Object source, String name, Object... list) throws Exception {
      FunctionPointer call = matcher.match(source, name, list);
      
      if(call != null) {
         return new FunctionCall(call, scope, source);
      }
      return null;
   }
}
