package org.snapscript.core.index;

import static org.snapscript.core.Reserved.TYPE_CLASS;
import static org.snapscript.core.ModifierType.STATIC;
import static org.snapscript.core.ModifierType.CONSTANT;

import org.snapscript.core.InternalStateException;
import org.snapscript.core.Property;
import org.snapscript.core.Type;

public class ClassProperty implements Property {
   
   private final Type type;
   
   public ClassProperty(Type type) {
      this.type = type;
   }

   @Override
   public Type getType() {
      return type;
   }

   @Override
   public String getName() {
      return TYPE_CLASS;
   }

   @Override
   public int getModifiers() {
      return STATIC.mask | CONSTANT.mask;
   }

   @Override
   public Object getValue(Object source) {
      return type;
   }

   @Override
   public void setValue(Object source, Object value) {
      throw new InternalStateException("Illegal modification of constant " + TYPE_CLASS);
   }

}
