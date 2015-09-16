package org.snapscript.core.execute;

import java.util.ArrayList;
import java.util.List;

import org.snapscript.core.Holder;
import org.snapscript.core.Scope;
import org.snapscript.core.Value;
import org.snapscript.parse.StringToken;

public class ConstructList implements Evaluation {
   
   private final StringToken token;
   private final ArgumentList list;
  
   public ConstructList(StringToken token) {
      this(null, token);
   }
   
   public ConstructList(ArgumentList list) {
      this(list, null);
   }
   
   public ConstructList(ArgumentList list, StringToken token) {
      this.token = token;
      this.list = list;
   }
   
   @Override
   public Value evaluate(Scope scope, Object context) throws Exception { // this is rubbish
      List result = new ArrayList();
      
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