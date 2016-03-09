package org.snapscript.core.bind;

import java.util.List;

import org.snapscript.common.Cache;
import org.snapscript.common.LeastRecentlyUsedCache;
import org.snapscript.core.Function;
import org.snapscript.core.ModifierType;
import org.snapscript.core.Module;
import org.snapscript.core.Scope;
import org.snapscript.core.Signature;
import org.snapscript.core.State;
import org.snapscript.core.Type;
import org.snapscript.core.TypeExtractor;
import org.snapscript.core.TypeLoader;
import org.snapscript.core.Value;
import org.snapscript.core.convert.ConstraintMatcher;
import org.snapscript.core.error.ThreadStack;

public class FunctionMatcher {
   
   private final Cache<Object, Function> instance;
   private final Cache<Object, Function> cache;
   private final FunctionKeyBuilder builder;
   private final FunctionPathFinder finder;
   private final ArgumentMatcher matcher;
   private final TypeExtractor extractor;
   private final ThreadStack stack;
   
   public FunctionMatcher(ConstraintMatcher matcher, TypeLoader loader, ThreadStack stack) {
      this(matcher, loader, stack, 50000);
   }
   
   public FunctionMatcher(ConstraintMatcher matcher, TypeLoader loader, ThreadStack stack, int capacity) {
      this.instance = new LeastRecentlyUsedCache<Object, Function>(capacity);
      this.cache = new LeastRecentlyUsedCache<Object, Function>(capacity);
      this.matcher = new ArgumentMatcher(matcher, loader, capacity);
      this.builder = new FunctionKeyBuilder(loader);
      this.extractor = new TypeExtractor(loader);
      this.finder = new FunctionPathFinder();
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

      if(!cache.contains(key)) {
         List<Function> functions = module.getFunctions();
         Function function = null;
         
         if(!functions.isEmpty()) {
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
         }
         cache.cache(key, function);
      }
      Function function = cache.fetch(key); // static and module functions
      
      if(function != null) {
         Signature signature = function.getSignature();
         ArgumentConverter converter = matcher.match(signature);
         
         return new FunctionPointer(function, converter, stack, values);
      }
      return null;
   }
   
   public FunctionPointer match(Type type, String name, Object... values) throws Exception { 
      Object key = builder.create(type, name, values); 

      if(!cache.contains(key)) {
         List<Type> path = finder.findPath(type, name);
         Function function = null;
         
         if(!path.isEmpty()) {
            int best = 0;
            
            for(Type entry : path) {
               List<Function> functions = entry.getFunctions();
               int size = functions.size();
               
               for(int i = size - 1; i >= 0; i--) {
                  Function next = functions.get(i);
                  int modifiers = next.getModifiers();
                  
                  if(ModifierType.isStatic(modifiers)) { // must be static
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
            }
         }
         cache.cache(key, function);
      }
      Function function = cache.fetch(key); // static and module functions
         
      if(function != null) {
         Signature signature = function.getSignature();
         ArgumentConverter converter = matcher.match(signature);
         
         return new FunctionPointer(function, converter, stack, values);
      }
      return null;
   }
   
   public FunctionPointer match(Object value, String name, Object... values) throws Exception { 
      Type type = extractor.extract(value);
      Object key = builder.create(type, name, values);
      
      if(!instance.contains(key)) {
         List<Type> path = finder.findPath(type, name);
         Function function = null;
         
         if(!path.isEmpty()) {
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
         }
         instance.cache(key, function);
      }
      Function function = instance.fetch(key); // all type functions
      
      if(function != null) {
         Signature signature = function.getSignature();
         ArgumentConverter converter = matcher.match(signature);
         
         return new FunctionPointer(function, converter, stack, values);
      }
      return null;
   }
}
