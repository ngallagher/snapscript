package org.snapscript.interpret;

import java.util.Arrays;
import java.util.List;

import org.snapscript.core.Holder;
import org.snapscript.core.Scope;
import org.snapscript.core.Value;

public class MapEntry implements Evaluation {
   
   private final Evaluation value;
   private final Evaluation key;
   
   public MapEntry(Evaluation key, Evaluation value) {
      this.value = value;
      this.key = key;
   }
   
   public Value evaluate(Scope scope, Object left) throws Exception{
      Value valueResult = value.evaluate(scope, left);
      Value keyResult = key.evaluate(scope, left);
      Object valueObject = valueResult.getValue();
      Object keyObject = keyResult.getValue();
      List list = Arrays.asList(keyObject, valueObject);
      
      return new Holder(list);
   }
}