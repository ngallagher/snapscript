package org.snapscript.compile.instruction.loop;

import org.snapscript.compile.instruction.Evaluation;
import org.snapscript.compile.instruction.collection.Iteration;
import org.snapscript.compile.instruction.collection.IterationConverter;
import org.snapscript.core.Reference;
import org.snapscript.core.Result;
import org.snapscript.core.ResultFlow;
import org.snapscript.core.Scope;
import org.snapscript.core.State;
import org.snapscript.core.Statement;
import org.snapscript.core.Value;

public class ForInStatement extends Statement {
   
   private final IterationConverter converter;
   private final Evaluation identifier;
   private final Evaluation collection;
   private final Statement statement;

   public ForInStatement(Evaluation identifier, Evaluation collection, Statement statement) {
      this.converter = new IterationConverter();
      this.identifier = identifier;
      this.collection = collection;
      this.statement = statement;
   }

   @Override
   public Result execute(Scope scope) throws Exception {     
      Value reference = identifier.evaluate(scope, null);
      Value list = collection.evaluate(scope, null);
      String name = reference.getString();
      Object value = list.getValue();
      Iteration iteration = converter.convert(value);
      Iterable iterable = iteration.getIterable(scope);
      State state = scope.getState();
      
      for (Object entry : iterable) {
         Value variable = state.getValue(name);
         
         if(variable == null) {
            Reference constant = new Reference(entry);
            state.addVariable(name, constant);
         } else {
            variable.setValue(entry);
         }
         Result result = statement.execute(scope);
         ResultFlow flow = result.getFlow();    

         if (flow == ResultFlow.RETURN || flow == ResultFlow.THROW) {
            return result;
         }
         if (flow == ResultFlow.BREAK) {
            return new Result();
         }
      }    
      return new Result();
   }
}