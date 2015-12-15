package org.snapscript.compile.instruction.define;

import static org.snapscript.core.Reserved.TYPE_CLASS;
import static org.snapscript.core.Reserved.TYPE_THIS;

import org.snapscript.core.Initializer;
import org.snapscript.core.Property;
import org.snapscript.core.Result;
import org.snapscript.core.ResultType;
import org.snapscript.core.Scope;
import org.snapscript.core.ScopeAccessor;
import org.snapscript.core.Type;

public class DefaultInitializer extends Initializer {
   
   private final ScopeAccessor type;
   private final ScopeAccessor self;
   
   public DefaultInitializer() {
      this.type = new ScopeAccessor(TYPE_CLASS);
      this.self = new ScopeAccessor(TYPE_THIS);
   }

   @Override
   public Result execute(Scope scope, Type real) throws Exception {  
      Property property = new Property(TYPE_THIS, real, self);
      Property property2 = new Property(TYPE_CLASS, real, type);      

      real.getProperties().add(property);
      real.getProperties().add(property2);

      return ResultType.getNormal();
   }
}
