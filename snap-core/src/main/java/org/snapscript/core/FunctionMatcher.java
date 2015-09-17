package org.snapscript.core;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.snapscript.common.LeastRecentlyUsedMap;
import org.snapscript.core.Function;
import org.snapscript.core.Signature;

public class FunctionMatcher {
   
   private final Map<Object, Function> cache;
   private final FunctionKeyBuilder builder;
   private final ArgumentMatcher matcher;
   
   public FunctionMatcher(TypeLoader loader) {
      this(loader, 50000);
   }
   
   public FunctionMatcher(TypeLoader loader, int capacity) {
      this.cache = new LeastRecentlyUsedMap<Object, Function>(capacity);
      this.matcher = new ArgumentMatcher(loader, capacity);
      this.builder = new FunctionKeyBuilder(loader);
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
         List<Type> hierarchy = type.getTypes();
         Iterator<Type> iterator = hierarchy.iterator();
         Type base = type;
         int best = 0;
         
         while(base != null) {
            List<Function> functions = base.getFunctions();
   
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
            if(!iterator.hasNext()) { // no more supers
               break;
            }
            base = iterator.next();
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
