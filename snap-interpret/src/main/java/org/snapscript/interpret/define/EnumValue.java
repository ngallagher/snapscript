package org.snapscript.interpret.define;

import org.snapscript.core.Initializer;
import org.snapscript.core.Scope;
import org.snapscript.core.Type;
import org.snapscript.interpret.ArgumentList;

public class EnumValue {
   
   private final ArgumentList list;
   private final EnumKey key;
   
   public EnumValue(EnumKey key) {
      this(key, null);
   }
   
   public EnumValue(EnumKey key, ArgumentList list) {    
      this.list = list;
      this.key = key;
   }

   public Initializer define(Scope scope, Type type, int index) throws Exception { 
      return new EnumInitializer(key, list, index);
   }
}