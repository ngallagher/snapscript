package org.snapscript.core.extend;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.snapscript.core.Context;
import org.snapscript.core.Function;
import org.snapscript.core.InternalException;
import org.snapscript.core.Type;

public class ExtensionRegistry {

   private final Map<Class, List<Function>> functions;
   private final Map<Class, Class> extensions;
   private final FunctionExtractor extractor;
   private final Context context;
   
   public ExtensionRegistry(Context context){
      this.functions = new ConcurrentHashMap<Class, List<Function>>();
      this.extensions = new ConcurrentHashMap<Class, Class>();
      this.extractor = new FunctionExtractor(context);
      this.context = context;
   }
   
   public void register(Class type, Class extension) {
      extensions.put(type, extension);
   }
   
   public void update(Type type) {
      Class real = type.getType();
      Class extension = extensions.get(real);
      
      if(extension != null) {
         try {
            List<Function> functions = type.getFunctions();
            Constructor constructor = extension.getDeclaredConstructor(Context.class);
            Object instance = constructor.newInstance(context);
            List<Function> list = extractor.extract(instance);
            
            for(Function function : list) {
               functions.add(function);
            }
         } catch(Exception e) {
            throw new InternalException("Could not extend " + type, e);
         }
      }
   }
}
