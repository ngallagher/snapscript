package org.snapscript.core;

public class ConstantAccessor implements Accessor<Object> {
   
   private final Constant constant;
   
   public ConstantAccessor(Constant constant) {
      this.constant = constant;
   }

   @Override
   public Object getValue(Object source) {
      return constant.getValue();
   }

   @Override
   public void setValue(Object source, Object value) {
      constant.setValue(value);
   }

}
