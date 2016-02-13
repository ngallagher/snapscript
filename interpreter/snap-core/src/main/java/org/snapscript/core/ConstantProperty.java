package org.snapscript.core;

public class ConstantProperty implements Property<Object> {

   private final Constant constant;
   private final String name;
   private final Type type;
   private final int modifiers;
   
   public ConstantProperty(String name, Type type, Object value, int modifiers){
      this.constant = new Constant(value, type);
      this.modifiers = modifiers;
      this.name = name;
      this.type = type;
   }
   
   @Override
   public Type getType(){
      return type;
   }
   
   @Override
   public String getName(){
      return name;
   }
   
   @Override
   public int getModifiers() {
      return modifiers;
   }
   
   @Override
   public Object getValue(Object source) {
      return constant.getValue();
   }

   @Override
   public void setValue(Object source, Object value) {
      constant.setValue(value);
   }
   
   @Override
   public String toString(){
      return name;
   }
}
