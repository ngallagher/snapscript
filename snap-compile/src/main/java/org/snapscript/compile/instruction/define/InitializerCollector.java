package org.snapscript.compile.instruction.define;

import java.util.ArrayList;
import java.util.List;

import org.snapscript.core.Initializer;
import org.snapscript.core.Result;
import org.snapscript.core.ResultFlow;
import org.snapscript.core.Scope;
import org.snapscript.core.Type;

public class InitializerCollector extends Initializer {
   
   private final List<Initializer> list;
   
   public InitializerCollector(){
      this.list = new ArrayList<Initializer>();
   }

   public void update(Initializer initializer) throws Exception {
      if(initializer != null) {         
         list.add(initializer);
      }
   }
   
   @Override
   public Result compile(Scope scope, Type type) throws Exception {
      Result last = new Result();

      for(Initializer initializer : list) {
         Result result = initializer.compile(scope, type);
         ResultFlow flow = result.getFlow();
         
         if(flow != ResultFlow.NORMAL){
            return result;
         }
         last = result;
      }
      return last;
   } 

   @Override
   public Result execute(Scope scope, Type type) throws Exception {
      Result last = new Result();

      for(Initializer initializer : list) {
         Result result = initializer.execute(scope, type);
         ResultFlow flow = result.getFlow();
         
         if(flow != ResultFlow.NORMAL){
            return result;
         }
         last = result;
      }
      return last;
   }              
}
