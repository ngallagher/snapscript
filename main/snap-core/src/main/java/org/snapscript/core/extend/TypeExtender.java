package org.snapscript.core.extend;

import java.io.File;
import java.util.concurrent.atomic.AtomicBoolean;

import org.snapscript.core.Context;
import org.snapscript.core.Type;

public class TypeExtender {
   
   private final ExtensionRegistry registry;
   private final AtomicBoolean register;
   
   public TypeExtender(Context context) {
      this.registry = new ExtensionRegistry(context);
      this.register = new AtomicBoolean();
   }
   
   public void extend(Type type){
      if(register.compareAndSet(false, true)) {
         registry.register(File.class, FileExtension.class);
      }
      registry.update(type);
   }

}
