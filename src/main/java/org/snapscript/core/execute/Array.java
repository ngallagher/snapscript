package org.snapscript.core.execute;

import java.util.List;

import org.snapscript.core.Holder;
import org.snapscript.core.Scope;
import org.snapscript.core.Value;

public class Array implements Evaluation {
   
   private final ArrayConverter converter;
   private final Evaluation identifier;
   
   public Array(Evaluation identifier) {
      this.converter = new ArrayConverter();
      this.identifier = identifier;
   }
   
   @Override
   public Value evaluate(Scope scope, Object left) throws Exception{
      Value reference = identifier.evaluate(scope, left);
      String name = reference.getString();
      
      if(left == null) {
         Value value = scope.getValue(name);
         Object object = value.getValue();
         Class type = object.getClass();
         
         if(type.isArray()) {
            List list = converter.convert(object);
            return new Holder(list);
         }
         return value;
      }        
      return new MemberReference(left, name);
   }  
   

}