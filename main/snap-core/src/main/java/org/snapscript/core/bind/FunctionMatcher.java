package org.snapscript.core.bind;

import java.util.List;
import java.util.Map;

import org.snapscript.common.LeastRecentlyUsedMap;
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
   
   private final Map<Object, Function> cache;
   private final TypePathBuilder finder;
   private final FunctionKeyBuilder builder;
   private final ArgumentMatcher matcher;
   private final ThreadStack stack;
   
   public FunctionMatcher(ConstraintMatcher matcher, TypeLoader loader, ThreadStack stack) {
      this(matcher, loader, stack, 50000);
   }
   
   public FunctionMatcher(ConstraintMatcher matcher, TypeLoader loader, ThreadStack stack, int capacity) {
      this.cache = new LeastRecentlyUsedMap<Object, Function>(capacity);
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
      Function function = cache.get(key);
      
      if(!cache.containsKey(key)) {
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
         cache.put(key, function);
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
      Function function = cache.get(key);
      
      if(!cache.containsKey(key)) {
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
         cache.put(key, function);
      }
      if(function != null) {
         Signature signature = function.getSignature();
         ArgumentConverter converter = matcher.match(signature);
         
         return new FunctionPointer(function, converter, stack, values);
      }
      return null;
   }
}
