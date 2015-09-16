package org.snapscript.core.convert;

import java.util.concurrent.Callable;

import org.snapscript.core.Module;
import org.snapscript.core.Scope;
import org.snapscript.core.Type;
import org.snapscript.core.TypeLoader;
import org.snapscript.core.execute.Result;

public class FunctionBinder {
   
   private final FunctionMatcher matcher;
   private final TypeExtractor extractor;
   
   public FunctionBinder(TypeLoader loader) {
      this.matcher = new FunctionMatcher(loader);
      this.extractor = new TypeExtractor(loader);
   }
   
   public Callable<Result> bind(Scope scope, Module module, String name, Object... list) throws Exception {
      FunctionPointer call = matcher.match(module, name, list);
      
      if(call != null) {
         return new FunctionCall(call, scope, module, name);
      }
      return null;
   }
   
   public Callable<Result> bind(Scope scope, Type type, String name, Object... list) throws Exception {
      FunctionPointer call = matcher.match(type, name, list);
      
      if(call != null) {
         return new FunctionCall(call, scope, null, name);
      }
      return null;
   }

   public Callable<Result> bind(Scope scope, Object source, String name, Object... list) throws Exception {
      Type type = extractor.extract(source);
      FunctionPointer call = matcher.match(type, name, list);
      
      if(call != null) {
         return new FunctionCall(call, scope, source, name);
      }
      return null;
   }
}
