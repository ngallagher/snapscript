package org.snapscript.core.bind;

import java.util.List;
import java.util.Map;

import org.snapscript.common.LeastRecentlyUsedMap;
import org.snapscript.core.Function;
import org.snapscript.core.Module;
import org.snapscript.core.Signature;
import org.snapscript.core.Type;
import org.snapscript.core.TypeLoader;
import org.snapscript.core.convert.ConstraintMatcher;

public class FunctionMatcher {
   
   private final Map<Object, Function> cache;
   private final TypePathBuilder finder;
   private final FunctionKeyBuilder builder;
   private final ArgumentMatcher matcher;
   
   public FunctionMatcher(ConstraintMatcher matcher, TypeLoader loader) {
      this(matcher, loader, 50000);
   }
   
   public FunctionMatcher(ConstraintMatcher matcher, TypeLoader loader, int capacity) {
      this.cache = new LeastRecentlyUsedMap<Object, Function>(capacity);
      this.matcher = new ArgumentMatcher(matcher, loader, capacity);
      this.builder = new FunctionKeyBuilder(loader);
      this.finder = new TypePathBuilder();
   }

   public FunctionPointer match(Module module, String name, Object... values) throws Exception {
      Object key = builder.create(module, name, values);
      Function function = cache.get(key);
      
      if(!cache.containsKey(key)) {
         List<Function> functions = module.getFunctions();
         int best = 0;
   
         for(Function next : functions) {
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
         
         return new FunctionPointer(function, converter, values);
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
   
            for(Function next : functions) {
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
         
         return new FunctionPointer(function, converter, values);
      }
      return null;
   }
}
