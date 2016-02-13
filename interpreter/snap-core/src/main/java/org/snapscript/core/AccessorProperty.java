package org.snapscript.core;

public class AccessorProperty<T> implements Property<T> {

   private final Accessor<T> accessor;
   private final String name;
   private final Type type;
   private final int modifiers;
   
   public AccessorProperty(String name, Type type, Accessor<T> accessor, int modifiers){
      this.modifiers = modifiers;
      this.accessor = accessor;
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
   public Object getValue(T source) {
      return accessor.getValue(source);
   }
   
   @Override
   public void setValue(T source, Object value) {
      accessor.setValue(source, value);;
   }
   
   @Override
   public String toString(){
      return name;
   }
}
