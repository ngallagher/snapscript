package org.snapscript.interpret;

import java.util.LinkedHashSet;
import java.util.Set;

import org.snapscript.core.Holder;
import org.snapscript.core.Scope;
import org.snapscript.core.Value;
import org.snapscript.parse.StringToken;

public class ConstructSet implements Evaluation {

   private final StringToken token;
   private final ArgumentList list;
  
   public ConstructSet(StringToken token) {
      this(null, token);
   }
   
   public ConstructSet(ArgumentList list) {
      this(list, null);
   }
   
   public ConstructSet(ArgumentList list, StringToken token) {
      this.token = token;
      this.list = list;
   }   
   
   @Override
   public Value evaluate(Scope scope, Object context) throws Exception { // this is rubbish
      Set result = new LinkedHashSet();
      
      if(list != null) {
         Value reference = list.evaluate(scope, context);
         Object[] array = reference.getValue();
         
         for(Object value : array) {
            result.add(value);
         }         
      }   
      return new Holder(result);
   }
}