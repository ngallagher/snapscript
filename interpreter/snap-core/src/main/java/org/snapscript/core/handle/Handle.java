package org.snapscript.core.handle;

import org.snapscript.core.Type;

public class Handle {

   private final String name;
   private final Type type;
   
   public Handle(String name, Type type) {
      this.name = name;
      this.type = type;
   }
   
   @Override
   public boolean equals(Object key) {
      if(key instanceof Handle) {
         return equals((Handle)key);
      }
      return false;
   }
   
   public boolean equals(Handle key) {
      if(key.type != type) {
         return false;
      }
      return key.name.equals(name);
   }
   
   @Override
   public int hashCode() {
      if(type != null) {
         return name.hashCode() ^ type.hashCode();
      }
      return name.hashCode();
   }
   
   @Override
   public String toString() {
      return name;
   }
}
