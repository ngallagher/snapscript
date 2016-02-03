package org.snapscript.compile.instruction.define;

import static org.snapscript.core.ModifierType.CONSTANT;

import java.util.List;

import org.snapscript.core.Initializer;
import org.snapscript.core.Property;
import org.snapscript.core.Result;
import org.snapscript.core.ResultType;
import org.snapscript.core.Scope;
import org.snapscript.core.ScopeAccessor;
import org.snapscript.core.Type;

public class PropertyInitializer extends Initializer {
   
   private final String[] names;
   
   public PropertyInitializer(String... names) {
      this.names = names;
   }

   @Override
   public Result execute(Scope scope, Type type) throws Exception {  
      List<Property> properties = type.getProperties();
      
      for(String name : names) {
         ScopeAccessor accessor = new ScopeAccessor(name);
         Property property = new Property(name, type, accessor, CONSTANT.mask);
         
         properties.add(property);
      }
      return ResultType.getNormal();
   }
}
