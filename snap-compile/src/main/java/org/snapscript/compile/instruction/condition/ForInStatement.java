package org.snapscript.compile.instruction.condition;

import org.snapscript.compile.instruction.Evaluation;
import org.snapscript.compile.instruction.NameExtractor;
import org.snapscript.compile.instruction.collection.Iteration;
import org.snapscript.compile.instruction.collection.IterationConverter;
import org.snapscript.core.Result;
import org.snapscript.core.ResultType;
import org.snapscript.core.Scope;
import org.snapscript.core.State;
import org.snapscript.core.Statement;
import org.snapscript.core.Value;
import org.snapscript.core.ValueType;

public class ForInStatement extends Statement {
   
   private final IterationConverter converter;
   private final NameExtractor extractor;
   private final Evaluation collection;
   private final Statement statement;

   public ForInStatement(Evaluation identifier, Evaluation collection, Statement statement) {
      this.extractor = new NameExtractor(identifier);
      this.converter = new IterationConverter();
      this.collection = collection;
      this.statement = statement;
   }

   @Override
   public Result execute(Scope scope) throws Exception { 
      Value list = collection.evaluate(scope, null);
      String name = extractor.extract(scope);
      Object object = list.getValue();
      Iteration iteration = converter.convert(object);
      Iterable iterable = iteration.getIterable(scope);
      State state = scope.getState();
      
      for (Object entry : iterable) {
         Value variable = state.getValue(name);
         
         if(variable == null) {
            Value value = ValueType.getReference(entry);
            state.addVariable(name, value);
         } else {
            variable.setValue(entry);
         }
         Result result = statement.execute(scope);   

         if (result.isReturn() || result.isThrow()) {
            return result;
         }
         if (result.isBreak()) {
            return ResultType.getNormal();
         }
      }    
      return ResultType.getNormal();
   }
}