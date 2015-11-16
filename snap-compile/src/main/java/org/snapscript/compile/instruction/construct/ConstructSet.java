package org.snapscript.compile.instruction.construct;

import java.util.LinkedHashSet;
import java.util.Set;

import org.snapscript.compile.instruction.ArgumentList;
import org.snapscript.compile.instruction.Evaluation;
import org.snapscript.core.Holder;
import org.snapscript.core.Scope;
import org.snapscript.core.Value;
import org.snapscript.core.convert.ProxyBuilder;
import org.snapscript.parse.StringToken;

public class ConstructSet implements Evaluation {

   private final ProxyBuilder builder;
   private final StringToken token;
   private final ArgumentList list;
  
   public ConstructSet(StringToken token) {
      this(null, token);
   }
   
   public ConstructSet(ArgumentList list) {
      this(list, null);
   }
   
   public ConstructSet(ArgumentList list, StringToken token) {
      this.builder = new ProxyBuilder();
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
            Object proxy = builder.create(value);
            result.add(proxy);
         }         
      }   
      return new Holder(result);
   }
}