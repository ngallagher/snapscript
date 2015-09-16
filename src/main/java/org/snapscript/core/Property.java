package org.snapscript.core;

public class Property {
   
   private final Accessor accessor;
   private final String name;
   private final Type type;
   
   public Property(String name, Type type, Accessor accessor){
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
   
   @Override
   public String toString(){
      return name;
   }
}
