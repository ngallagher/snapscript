package org.snapscript.interpret.define;

import org.snapscript.core.Initializer;
import org.snapscript.core.Result;
import org.snapscript.core.Scope;
import org.snapscript.core.Statement;
import org.snapscript.core.Type;
import org.snapscript.interpret.CompoundStatement;

public class StatementInitializer extends Initializer {

   private final Statement statement;
   
   public StatementInitializer(Statement... statements) {
      this.statement = new CompoundStatement(statements);
   }
   
   @Override
   public Result execute(Scope scope, Type type) throws Exception {
      return statement.execute(scope);
   }

   
}
