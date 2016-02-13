package org.snapscript.core;

public class ScopeProperty implements Property<Scope> {

   private final Accessor<Scope> accessor;
   private final String name;
   private final Type type;
   private final int modifiers;
   
   public ScopeProperty(String name, Type type, int modifiers){
      this.accessor = new ScopeAccessor(name);
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
   public Object getValue(Scope source) {
      return accessor.getValue(source);
   }

   @Override
   public void setValue(Scope source, Object value) {
      accessor.setValue(source, value);
   }
   
   @Override
   public String toString(){
      return name;
   }
}
