package org.snapscript.core;

public class Property {
   
   private final Accessor accessor;
   private final String name;
   private final Type type;
   private final int modifiers;
   
   public Property(String name, Type type, Accessor accessor, int modifiers){
      this.modifiers = modifiers;
      this.accessor = accessor;
      this.name = name;
      this.type = type;
   }
   
   public Type getType(){
      return type;
   }
   
   public String getName(){
      return name;
   }
   
   public Accessor getAccessor(){
      return accessor;
   }
   
   public int getModifiers() {
      return modifiers;
   }
   
   @Override
   public String toString(){
      return name;
   }
}
