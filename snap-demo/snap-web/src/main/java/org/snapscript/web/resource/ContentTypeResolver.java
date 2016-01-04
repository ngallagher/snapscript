package org.snapscript.web.resource;

import java.io.File;
import java.util.Map;
import java.util.Set;

import org.snapscript.common.Cache;
import org.snapscript.common.LeastRecentlyUsedCache;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedOperationParameter;
import org.springframework.jmx.export.annotation.ManagedOperationParameters;
import org.springframework.jmx.export.annotation.ManagedResource;

@ManagedResource(description="Resolve content type by path")
public class ContentTypeResolver {

   private final Cache<String, String> cache;
   private final Map<String, String> types;

   public ContentTypeResolver(Map<String, String> types) {
      this(types, 10000);
   }
   
   public ContentTypeResolver(Map<String, String> types, int capacity) {
      this.cache = new LeastRecentlyUsedCache<String, String>(capacity);
      this.types = types;
   }
   
   @ManagedOperation(description="Resolve type for path")
   @ManagedOperationParameters({
      @ManagedOperationParameter(name="path", description="Path to match")
   })
   public String resolveType(String path) {
      Set<String> expressions = types.keySet();
      String token = path.toLowerCase();

      for (String expression : expressions) {         
         if (token.matches(expression)) {
            String type = types.get(expression);
            
            if(type != null) {             
               return type;
            }
         }
      }
      return "application/octet-stream";
   }   

   public String resolveType(File file) {
      String path = file.getName();
      String result = cache.fetch(path);
      
      if(result == null) {
         String type = resolveType(path);
         
         if(type != null) {
            cache.cache(path, type);
            return type;
         }
      }
      return result;
   }
}
