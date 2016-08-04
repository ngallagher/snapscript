package org.snapscript.code.construct;

import org.snapscript.code.NameExtractor;
import org.snapscript.core.Evaluation;
import org.snapscript.core.Scope;
import org.snapscript.core.State;
import org.snapscript.core.Value;
import org.snapscript.core.ValueType;

public class MapKey implements Evaluation {
   
   private final NameExtractor extractor;
   
   public MapKey(Evaluation key) {
      this.extractor = new NameExtractor(key);
   }
   
   @Override
   public Value evaluate(Scope scope, Object left) throws Exception{
      String name = extractor.extract(scope);
      State state = scope.getState();
      Value value = state.getValue(name);
      
      if(value == null) {
         return ValueType.getTransient(name);
      }
      return value;
   }
}