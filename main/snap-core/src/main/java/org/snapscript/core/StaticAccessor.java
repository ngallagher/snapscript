package org.snapscript.core;

public class StaticAccessor implements Accessor {

   private final Initializer initializer;
   private final Accessor accessor;
   private final Scope scope;
   private final String name;
   private final Type type;
   
   public StaticAccessor(Initializer initializer, Scope scope, Type type, String name) {
      this.accessor = new ScopeAccessor(name);
      this.initializer = initializer;
      this.scope = scope;
      this.name = name;
      this.type = type;
   }
   
   @Override
   public Object getValue(Object source) {
      try {
         State state = scope.getState();
         Value field = state.getValue(name);
         
         if(field == null) {
            initializer.compile(scope, type);           
         }
      }catch(Exception e){
         throw new InternalStateException("Static reference of '" + name + "' in '" + type + "' failed", e);
      }
      return accessor.getValue(scope);
   }

   @Override
   public void setValue(Object source, Object value) {
      try {
         State state = scope.getState();
         Value field = state.getValue(name);
         
         if(field == null) {
            initializer.compile(scope, type);           
         }    
      }catch(Exception e){
         throw new InternalStateException("Static reference of '" + name + "' in '" + type + "' failed", e);
      }   
      accessor.setValue(scope,value);
   }

}
