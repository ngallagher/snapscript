package org.snapscript.compile.instruction.define;

import org.snapscript.compile.instruction.ArgumentList;
import org.snapscript.core.Bug;
import org.snapscript.core.Initializer;
import org.snapscript.core.Scope;
import org.snapscript.core.Type;

public class ThisConstructor implements TypePart {
   
   private final ArgumentList list;
   
   public ThisConstructor() {
      this(null);
   }
   
   public ThisConstructor(ArgumentList list) {
      this.list = list;
   }

   @Bug("This is rubbish and needs to be cleaned up")
   @Override
   public Initializer define(Scope scope, Initializer statement, Type type) throws Exception {
      return null;
   }
}