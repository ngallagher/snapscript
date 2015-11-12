package org.snapscript.interpret;

import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;

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
      
      if(keyObject != null) {
         Class type = keyObject.getClass();
         String key = keyObject.toString();
         
         if(type == String.class) {
            Value value = scope.getValue(key);
            
            if(value != null) {
               keyObject = value.getValue();
            }
         }
      }
      Entry entry = new Pair(keyObject, valueObject);
      Holder holder =  new Holder(entry);
      
      return holder;
   }
   
   private class Pair implements Entry {
      
      private final Object value;
      private final Object key;
      
      public Pair(Object key, Object value) {
         this.value = value;
         this.key = key;
      }

      @Override
      public Object getKey() {
         return key;
      }

      @Override
      public Object getValue() {
         return value;
      }

      @Override
      public Object setValue(Object value) {
         throw new IllegalStateException("Modification of constant entry '" + key + "'");
      }
      
   }
}