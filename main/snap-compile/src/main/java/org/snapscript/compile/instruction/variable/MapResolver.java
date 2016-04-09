package org.snapscript.compile.instruction.variable;

import java.util.Map;

import org.snapscript.compile.instruction.collection.MapValue;
import org.snapscript.core.Context;
import org.snapscript.core.Scope;
import org.snapscript.core.Value;
import org.snapscript.core.convert.ProxyWrapper;

public class MapResolver implements ValueResolver<Map> {
   
   private final String name;
   
   public MapResolver(String name) {
      this.name = name;
   }
   
   @Override
   public Value resolve(Scope scope, Map left) {
      Context context = scope.getContext();
      ProxyWrapper wrapper = context.getWrapper();
      
      return new MapValue(wrapper, left, name);
   }
}