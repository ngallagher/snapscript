package org.snapscript.core.execute;

import org.snapscript.core.Reference;
import org.snapscript.core.Scope;
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

      for (Object entry : iterable) {
         Value variable = scope.getValue(name);
         
         if(variable == null) {
            Reference constant = new Reference(entry);
            scope.addVariable(name, constant);
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