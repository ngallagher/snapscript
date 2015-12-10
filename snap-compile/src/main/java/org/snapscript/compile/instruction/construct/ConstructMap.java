package org.snapscript.compile.instruction.construct;

import java.util.LinkedHashMap;
import java.util.Map;

import org.snapscript.compile.instruction.Evaluation;
import org.snapscript.core.Transient;
import org.snapscript.core.Scope;
import org.snapscript.core.Value;
import org.snapscript.parse.StringToken;

public class ConstructMap implements Evaluation {

   private final StringToken token;
   private final MapEntryList list;
   
   public ConstructMap(StringToken token) {
      this(null, token);
   }
   
   public ConstructMap(MapEntryList list) {
      this(list, null);
   }
   
   public ConstructMap(MapEntryList list, StringToken token) {
      this.token = token;
      this.list = list;
   }   
   
   @Override
   public Value evaluate(Scope scope, Object context) throws Exception { // this is rubbish
      Map map = new LinkedHashMap();
      
      if(list != null) {
         return list.evaluate(scope, context);
      }
      return new Transient(map);
   }
}