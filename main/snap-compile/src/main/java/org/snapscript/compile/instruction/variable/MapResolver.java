package org.snapscript.compile.instruction.variable;

import java.util.Map;

import org.snapscript.compile.instruction.collection.MapValue;
import org.snapscript.core.Context;
import org.snapscript.core.Scope;
import org.snapscript.core.Value;
import org.snapscript.core.convert.ProxyWrapper;

public class MapResolver implements ValueResolver<Object> {
   
   private final String name;
   
   public MapResolver(String name) {
      this.name = name;
   }
   
   @Override
   public Value resolve(Scope scope, Object left) {
      Map map = (Map)left;
      Context context = scope.getContext();
      ProxyWrapper wrapper = context.getWrapper();
      
      return new MapValue(wrapper, map, name);
   }
}