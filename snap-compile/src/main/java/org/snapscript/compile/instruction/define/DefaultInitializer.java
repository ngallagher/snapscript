package org.snapscript.compile.instruction.define;

import static org.snapscript.core.ResultFlow.NORMAL;

import org.snapscript.core.Initializer;
import org.snapscript.core.Property;
import org.snapscript.core.Result;
import org.snapscript.core.Scope;
import org.snapscript.core.ScopeAccessor;
import org.snapscript.core.Type;

public class DefaultInitializer extends Initializer {
   
   private final ScopeAccessor type;
   private final ScopeAccessor self;
   
   public DefaultInitializer() {
      this.type = new ScopeAccessor("class");
      this.self = new ScopeAccessor("this");
   }

   @Override
   public Result execute(Scope scope, Type real) throws Exception {  
      Property property = new Property("this", real, self);
      Property property2 = new Property("class", real, type);      

      real.getProperties().add(property);
      real.getProperties().add(property2);

      return NORMAL.getResult();
   }
}
