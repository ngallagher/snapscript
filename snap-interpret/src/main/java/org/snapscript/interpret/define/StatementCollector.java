package org.snapscript.interpret.define;

import java.util.ArrayList;
import java.util.List;

import org.snapscript.core.Result;
import org.snapscript.core.ResultFlow;
import org.snapscript.core.Scope;
import org.snapscript.core.Statement;

public class StatementCollector extends Statement {
   
   private final List<Statement> list;
   
   public StatementCollector(){
      this.list = new ArrayList<Statement>();
   }

   public void update(Statement statement) throws Exception {
      if(statement != null) {         
         list.add(statement);
      }
   }

   @Override
   public Result execute(Scope scope) throws Exception {
      Result last = new Result();

      for(Statement statement : list) {
         Result result = statement.compile(scope);
         ResultFlow type = result.getFlow();
         
         if(type != ResultFlow.NORMAL){
            return result;
         }
      }
      for(Statement statement : list) {
         Result result = statement.execute(scope);
         ResultFlow type = result.getFlow();
         
         if(type != ResultFlow.NORMAL){
            return result;
         }
         last = result;
      }
      return last;
   }              
}
