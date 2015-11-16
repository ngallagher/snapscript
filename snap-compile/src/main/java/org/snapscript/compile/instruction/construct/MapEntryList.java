package org.snapscript.compile.instruction.construct;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.snapscript.compile.instruction.Evaluation;
import org.snapscript.core.Holder;
import org.snapscript.core.Scope;
import org.snapscript.core.Value;
import org.snapscript.core.convert.ProxyBuilder;

public class MapEntryList implements Evaluation{
   
   private final ProxyBuilder builder;
   private final MapEntry[] list;
   
   public MapEntryList(MapEntry... list) {
      this.builder = new ProxyBuilder();
      this.list = list;
   }
   
   @Override
   public Value evaluate(Scope scope, Object left) throws Exception{
      Map map = new LinkedHashMap();
      
      for(int i = 0; i < list.length; i++){
         Value entry = list[i].evaluate(scope, left);
         Entry pair = entry.getValue();
         Object key = pair.getKey();
         Object value = pair.getValue();
         Object keyProxy = builder.create(key);
         Object valueProxy = builder.create(value);
         
         map.put(keyProxy, valueProxy);
      }
      return new Holder(map);
   }
}