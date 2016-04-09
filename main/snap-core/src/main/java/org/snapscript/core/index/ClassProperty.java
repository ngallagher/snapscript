package org.snapscript.core.index;

import static org.snapscript.core.ModifierType.CONSTANT;
import static org.snapscript.core.ModifierType.STATIC;
import static org.snapscript.core.Reserved.TYPE_CLASS;

import org.snapscript.core.InternalStateException;
import org.snapscript.core.Property;
import org.snapscript.core.Type;

public class ClassProperty implements Property {
   
   private final Type constraint;
   private final Type type;
   
   public ClassProperty(Type type, Type constraint) {
      this.constraint = constraint;
      this.type = type;
   }

   @Override
   public Type getType() {
      return type;
   }
   
   @Override
   public Type getConstraint() {
      return constraint;
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
