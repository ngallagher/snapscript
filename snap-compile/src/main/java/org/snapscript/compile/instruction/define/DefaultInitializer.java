package org.snapscript.compile.instruction.define;

import static org.snapscript.core.Reserved.TYPE_CLASS;
import static org.snapscript.core.Reserved.TYPE_THIS;

import java.util.List;

import org.snapscript.core.Initializer;
import org.snapscript.core.Property;
import org.snapscript.core.Result;
import org.snapscript.core.ResultType;
import org.snapscript.core.Scope;
import org.snapscript.core.ScopeAccessor;
import org.snapscript.core.Type;

public class DefaultInitializer extends Initializer {
   
   private final String[] names;

   public DefaultInitializer() {
      this(TYPE_CLASS, TYPE_THIS);
   }
   
   public DefaultInitializer(String... names) {
      this.names = names;
   }

   @Override
   public Result execute(Scope scope, Type type) throws Exception {  
      List<Property> properties = type.getProperties();
      
      for(String name : names) {
         ScopeAccessor accessor = new ScopeAccessor(name);
         Property property = new Property(name, type, accessor);
         
         properties.add(property);
      }
      return ResultType.getNormal();
   }
}
