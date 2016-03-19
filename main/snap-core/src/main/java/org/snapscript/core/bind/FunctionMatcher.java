package org.snapscript.core.bind;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.snapscript.core.EmptyFunction;
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
   
   private final Map<Object, Function> instance;
   private final Map<Object, Function> cache;
   private final FunctionKeyBuilder builder;
   private final FunctionPathFinder finder;
   private final ArgumentMatcher matcher;
   private final TypeExtractor extractor;
   private final ThreadStack stack;
   private final Function invalid;
   
   public FunctionMatcher(ConstraintMatcher matcher, TypeLoader loader, ThreadStack stack) {
      this.instance = new ConcurrentHashMap<Object, Function>();
      this.cache = new ConcurrentHashMap<Object, Function>();
      this.matcher = new ArgumentMatcher(matcher, loader);
      this.builder = new FunctionKeyBuilder(loader);
      this.extractor = new TypeExtractor(loader);
      this.finder = new FunctionPathFinder();
      this.invalid = new EmptyFunction(null);
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
      Function function = cache.get(key); // static and module functions
      
      if(function == null) {
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
         if(function == null) {
            function = invalid;
         }
         cache.put(key, function);
      }
      if(function != invalid) {
         Signature signature = function.getSignature();
         ArgumentConverter converter = matcher.match(signature);
         
         return new FunctionPointer(function, converter, stack, values);
      }
      return null;
   }
   
   public FunctionPointer match(Type type, String name, Object... values) throws Exception { 
      Object key = builder.create(type, name, values); 
      Function function = cache.get(key); // static and module functions
      
      if(function == null) {
         List<Type> path = finder.findPath(type, name);
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
         if(function == null) {
            function = invalid;
         }
         cache.put(key, function);
      }  
      if(function != invalid) {
         Signature signature = function.getSignature();
         ArgumentConverter converter = matcher.match(signature);
         
         return new FunctionPointer(function, converter, stack, values);
      }
      return null;
   }
   
   public FunctionPointer match(Object value, String name, Object... values) throws Exception { 
      Type type = extractor.extract(value);
      Object key = builder.create(type, name, values);
      Function function = instance.get(key); // all type functions
      
      if(!instance.containsKey(key)) {
         List<Type> path = finder.findPath(type, name);
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
         if(function == null) {
            function = invalid;
         }
         instance.put(key, function); // this could be null?
      }      
      if(function != invalid) {
         Signature signature = function.getSignature();
         ArgumentConverter converter = matcher.match(signature);
         
         return new FunctionPointer(function, converter, stack, values);
      }
      return null;
   }
}
