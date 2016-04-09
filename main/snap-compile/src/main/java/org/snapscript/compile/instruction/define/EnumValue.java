package org.snapscript.compile.instruction.define;

import org.snapscript.compile.instruction.ArgumentList;
import org.snapscript.core.Initializer;
import org.snapscript.core.Scope;
import org.snapscript.core.Type;

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

   public Initializer compile(Scope scope, Type type, int index) throws Exception { 
      return new EnumInitializer(key, list, index);
   }
}