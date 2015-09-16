package org.snapscript.core;

public class ScopeAccessor implements Accessor<Scope> {

   private final String name;
   
   public ScopeAccessor(String name) {
      this.name = name;
   }
   
   @Override
   public Object getValue(Scope source) {
      Value field = source.getValue(name);
      
      if(field == null){
         throw new IllegalStateException("Field '" + name + "' does not exist");
      }
      return field.getValue();
   }

   @Override
   public void setValue(Scope source, Object value) {
      Value field = source.getValue(name);
      
      if(field == null){
         throw new IllegalStateException("Field '" + name + "' does not exist");
      }
      field.setValue(value);
   }

}
