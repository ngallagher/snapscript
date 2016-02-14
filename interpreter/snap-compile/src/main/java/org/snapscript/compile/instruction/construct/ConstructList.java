package org.snapscript.compile.instruction.construct;

import java.util.ArrayList;
import java.util.List;

import org.snapscript.compile.instruction.ArgumentList;
import org.snapscript.compile.instruction.Evaluation;
import org.snapscript.core.Scope;
import org.snapscript.core.Value;
import org.snapscript.core.ValueType;
import org.snapscript.core.convert.ProxyWrapper;
import org.snapscript.parse.StringToken;

public class ConstructList implements Evaluation {
   
   private final ArgumentList arguments;
   private final ProxyWrapper wrapper;
   private final StringToken token;
  
   public ConstructList(StringToken token) {
      this(null, token);
   }
   
   public ConstructList(ArgumentList arguments) {
      this(arguments, null);
   }
   
   public ConstructList(ArgumentList arguments, StringToken token) {
      this.wrapper = new ProxyWrapper();
      this.arguments = arguments;
      this.token = token;
   }
   
   @Override
   public Value evaluate(Scope scope, Object context) throws Exception { // this is rubbish
      List result = new ArrayList();
      
      if(arguments != null) {
         Value reference = arguments.evaluate(scope, context);
         Object[] array = reference.getValue();
         
         for(Object value : array) {
            Object proxy = wrapper.toProxy(value);
            result.add(proxy);
         }         
      }   
      return ValueType.getTransient(result);
   }
}