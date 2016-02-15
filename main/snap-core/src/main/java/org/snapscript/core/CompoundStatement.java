package org.snapscript.core;

public class CompoundStatement extends Statement {
   
   private final Statement[] statements;

   public CompoundStatement(Statement... statements) {
      this.statements = statements;
   }
   
   @Override
   public Result compile(Scope scope) throws Exception {
      Result last = null;
      
      for(Statement statement : statements) {
         Result result = statement.compile(scope);
         
         if(!result.isNormal()){
            return result;
         }
         last = result;
      }
      return last;
   }
   
   @Override
   public Result execute(Scope scope) throws Exception {
      Scope compound = scope.getInner(); 
      Result last = null;
      
      for(Statement statement : statements) {
         Result result = statement.execute(compound);
         
         if(!result.isNormal()){
            return result;
         }
         last = result;
      }
      if(last == null) {
         return ResultType.getNormal();
      }
      return last;
   }
}
