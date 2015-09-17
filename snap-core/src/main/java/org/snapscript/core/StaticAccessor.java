package org.snapscript.core;

public class StaticAccessor implements Accessor {

   private Statement statement;
   private Accessor accessor;
   private Scope scope;
   private String name;
   
   public StaticAccessor(Statement statement, Scope scope, String name) {
      this.accessor = new ScopeAccessor(name);
      this.statement = statement;
      this.scope = scope;
      this.name = name;
   }
   
   @Override
   public Object getValue(Object source) {
      try {
         Value v=scope.getValue(name);
         if(v==null) {
            statement.execute(scope);           
         }
      }catch(Exception e){
         throw new IllegalStateException(e);
      }

      return accessor.getValue(scope);
   }

   @Override
   public void setValue(Object source, Object value) {
      try {
         Value v=scope.getValue(name);
         if(v==null) {
            statement.execute(scope);           
         }     
      }catch(Exception e){
         throw new IllegalStateException(e);
      }   
      accessor.setValue(scope,value);
   }

}
