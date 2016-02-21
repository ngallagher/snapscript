package org.snapscript.core.bind;

import java.util.List;

import org.snapscript.common.Cache;
import org.snapscript.common.LeastRecentlyUsedCache;
import org.snapscript.core.Function;
import org.snapscript.core.Module;
import org.snapscript.core.Scope;
import org.snapscript.core.Signature;
import org.snapscript.core.State;
import org.snapscript.core.Type;
import org.snapscript.core.TypeLoader;
import org.snapscript.core.Value;
import org.snapscript.core.convert.ConstraintMatcher;
import org.snapscript.core.error.ThreadStack;

public class FunctionMatcher {
   
   private final Cache<Object, Function> cache;
   private final FunctionKeyBuilder builder;
   private final ArgumentMatcher matcher;
   private final TypePathBuilder finder;
   private final ThreadStack stack;
   
   public FunctionMatcher(ConstraintMatcher matcher, TypeLoader loader, ThreadStack stack) {
      this(matcher, loader, stack, 50000);
   }
   
   public FunctionMatcher(ConstraintMatcher matcher, TypeLoader loader, ThreadStack stack, int capacity) {
      this.cache = new LeastRecentlyUsedCache<Object, Function>(capacity);
      this.matcher = new ArgumentMatcher(matcher, loader, capacity);
      this.builder = new FunctionKeyBuilder(loader);
      this.finder = new TypePathBuilder();
      this.stack = stack;
   }
   
   public FunctionPointer match(Value value, Object... values) throws Exception { // match function variable
      Object object = value.getValue();
      
      if(Function.class.isInstance(object)) {
         Function function = (Function)object;
         Signature signature = function.getSignature();
         ArgumentConverter converter = matcher.match(signature);
         
         return new FunctionPointer(function, converter, stack, values); 
      }
      return null;
   }
   
   public FunctionPointer match(Scope scope, String name, Object... values) throws Exception { // match function variable
      State state = scope.getState();
      Value value = state.getValue(name);
      
      if(value != null) {
         Object object = value.getValue();
         
         if(Function.class.isInstance(object)) {
            Function function = (Function)object;
            Signature signature = function.getSignature();
            ArgumentConverter converter = matcher.match(signature);
            
            return new FunctionPointer(function, converter, stack, values); 
         }
      }
      return null;
   }

   public FunctionPointer match(Module module, String name, Object... values) throws Exception {
      Object key = builder.create(module, name, values);
      Function function = cache.fetch(key);
      
      if(!cache.contains(key)) {
         List<Function> functions = module.getFunctions();
         int size = functions.size();
         int best = 0;
   
         for(int i = size - 1; i >= 0; i--) { 
            Function next = functions.get(i);
            String method = next.getName();
   
            if(name.equals(method)) {
               Signature signature = next.getSignature();
               ArgumentConverter match = matcher.match(signature);
               int score = match.score(values);
   
               if(score > best) {
                  function = next;
                  best = score;
               }
            }
         }
         cache.cache(key, function);
      }
      if(function != null) {
         Signature signature = function.getSignature();
         ArgumentConverter converter = matcher.match(signature);
         
         return new FunctionPointer(function, converter, stack, values);
      }
      return null;
   }
   
   public FunctionPointer match(Type type, String name, Object... values) throws Exception { 
      Object key = builder.create(type, name, values);
      Function function = cache.fetch(key);
      
      if(!cache.contains(key)) {
         List<Type> path = finder.createPath(type, name);
         int best = 0;
         
         for(Type entry : path) {
            List<Function> functions = entry.getFunctions();
            int size = functions.size();
            
            for(int i = size - 1; i >= 0; i--) {
               Function next = functions.get(i);
               String method = next.getName();
   
               if(name.equals(method)) {
                  Signature signature = next.getSignature();
                  ArgumentConverter match = matcher.match(signature);
                  int score = match.score(values);
   
                  if(score > best) {
                     function = next;
                     best = score;
                  }
               }
            }
         }
         cache.cache(key, function);
      }
      if(function != null) {
         Signature signature = function.getSignature();
         ArgumentConverter converter = matcher.match(signature);
         
         return new FunctionPointer(function, converter, stack, values);
      }
      return null;
   }
}
