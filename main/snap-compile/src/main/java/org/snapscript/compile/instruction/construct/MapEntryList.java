package org.snapscript.compile.instruction.construct;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.snapscript.core.Context;
import org.snapscript.core.Evaluation;
import org.snapscript.core.Scope;
import org.snapscript.core.Value;
import org.snapscript.core.ValueType;
import org.snapscript.core.convert.ProxyWrapper;

public class MapEntryList implements Evaluation{
   
   private final MapEntry[] list;
   
   public MapEntryList(MapEntry... list) {
      this.list = list;
   }
   
   @Override
   public Value evaluate(Scope scope, Object left) throws Exception{
      Map map = new LinkedHashMap();
      
      for(int i = 0; i < list.length; i++){
         Value entry = list[i].evaluate(scope, left);
         Context context = scope.getContext();
         ProxyWrapper wrapper = context.getWrapper();
         Entry pair = entry.getValue();
         Object key = pair.getKey();
         Object value = pair.getValue();
         Object keyProxy = wrapper.toProxy(key);
         Object valueProxy = wrapper.toProxy(value);
         
         map.put(keyProxy, valueProxy);
      }
      return ValueType.getTransient(map);
   }
}