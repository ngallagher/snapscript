package org.snapscript.core.execute;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.snapscript.core.Holder;
import org.snapscript.core.Scope;
import org.snapscript.core.Value;

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
         List pair = entry.getValue();
         Object key = pair.get(0);
         Object value = pair.get(1);
         
         map.put(key, value);
      }
      return new Holder(map);
   }
}