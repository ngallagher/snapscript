package org.snapscript.core.execute;

import java.util.List;

import org.snapscript.core.Function;
import org.snapscript.core.Invocation;
import org.snapscript.core.Module;
import org.snapscript.core.Result;
import org.snapscript.core.ResultFlow;
import org.snapscript.core.Scope;
import org.snapscript.core.Signature;
import org.snapscript.core.Statement;
import org.snapscript.core.Value;

public class FunctionDeclaration extends Statement {
   
   private final ParameterList parameters;
   private final Evaluation identifier;
   private final Statement statement;
   
   public FunctionDeclaration(Evaluation identifier, ParameterList parameters, Statement statement){  
      this.identifier = identifier;
      this.parameters = parameters;
      this.statement = statement;
   }  
   
   @Override
   public Result compile(Scope scope) throws Exception {
      Module module = scope.getModule();
      List<Function> functions = module.getFunctions();
      Signature signature = parameters.create(scope);
      Value value = identifier.evaluate(scope, null);      
      String name = value.getString();
      Invocation invocation = new StatementInvocation(statement, signature);
      Function function = new Function(signature, invocation, name, name);// description is wrong here.....
      
      functions.add(function);
      return new Result(ResultFlow.NORMAL, function);
   }
}