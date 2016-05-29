package org.snapscript.core.extend;

import java.io.File;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.snapscript.core.Function;
import org.snapscript.core.TypeLoader;

public class ClassExtender {
   
   private final ExtensionRegistry registry;
   private final AtomicBoolean done;
   
   public ClassExtender(TypeLoader loader) {
      this.registry = new ExtensionRegistry(loader);
      this.done = new AtomicBoolean();
   }
   
   public List<Function> extend(Class type){
      if(done.compareAndSet(false, true)) {
         registry.register(File.class, FileExtension.class);
      }
      return registry.extract(type);
   }

}
