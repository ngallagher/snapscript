package org.snapscript.core.extend;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.snapscript.core.Function;
import org.snapscript.core.InternalException;
import org.snapscript.core.TypeLoader;

public class ExtensionRegistry {

   private final Map<Class, Class> extensions;
   private final FunctionExtractor extractor;
   
   public ExtensionRegistry(TypeLoader loader){
      this.extensions = new ConcurrentHashMap<Class, Class>();
      this.extractor = new FunctionExtractor(loader);
   }
   
   public void register(Class type, Class extension) {
      extensions.put(type, extension);
   }
   
   public List<Function> extract(Class type) {
      Class extension = extensions.get(type);
      
      if(extension != null) {
         try {
            Object instance = extension.newInstance();
            return extractor.extract(type, instance);
         } catch(Exception e) {
            throw new InternalException("Could not extend " + type, e);
         }
      }
      return Collections.emptyList();
   }
}
