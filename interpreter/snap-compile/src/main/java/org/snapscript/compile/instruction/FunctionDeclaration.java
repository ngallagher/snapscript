package org.snapscript.compile.instruction;

import java.util.List;

import org.snapscript.core.Bug;
import org.snapscript.core.Function;
import org.snapscript.core.Invocation;
import org.snapscript.core.Module;
import org.snapscript.core.Result;
import org.snapscript.core.ResultType;
import org.snapscript.core.Scope;
import org.snapscript.core.Signature;
import org.snapscript.core.Statement;

public class FunctionDeclaration extends Statement {
   
   private final ParameterList parameters;
   private final FunctionBuilder builder;
   private final NameExtractor extractor;
   
   public FunctionDeclaration(Evaluation identifier, ParameterList parameters, Statement statement){  
      this.extractor = new NameExtractor(identifier);
      this.builder = new FunctionBuilder(statement);
      this.parameters = parameters;
   }  
   
   @Override
   public Result compile(Scope scope) throws Exception {
      Module module = scope.getModule();
      List<Function> functions = module.getFunctions();
      Signature signature = parameters.create(scope);
      String name = extractor.extract(scope);
      Function function = builder.create(signature, name);
      
      functions.add(function);
      
      return ResultType.getNormal(function);
   }
   
   @Bug("see FunctionBuilder a,k,a TypeFunctionBuilder")
   private static class FunctionBuilder {

      private final Statement statement;
      
      public FunctionBuilder(Statement statement) {
         this.statement = statement;
      }

      public Function create(Signature signature, String name) {
         Invocation invocation = new StatementInvocation(statement, signature);
         return new Function(signature, invocation, name, 0);
      }
   }
}