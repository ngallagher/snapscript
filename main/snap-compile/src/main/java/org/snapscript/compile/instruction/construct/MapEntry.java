package org.snapscript.compile.instruction.construct;

import java.util.Map.Entry;

import org.snapscript.core.Evaluation;
import org.snapscript.core.InternalStateException;
import org.snapscript.core.Scope;
import org.snapscript.core.State;
import org.snapscript.core.Value;
import org.snapscript.core.ValueType;

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
      State state = scope.getState();
      
      if(keyObject != null) {
         Class type = keyObject.getClass();
         String key = keyObject.toString();
         
         if(type == String.class) {
            Value value = state.getValue(key);
            
            if(value != null) {
               keyObject = value.getValue();
            }
         }
      }
      Entry entry = new Pair(keyObject, valueObject);
      return ValueType.getTransient(entry);
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
         throw new InternalStateException("Modification of constant entry '" + key + "'");
      }
      
   }
}