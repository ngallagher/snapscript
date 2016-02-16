package org.snapscript.core.export;

import org.snapscript.core.Context;
import org.snapscript.core.ExpressionEvaluator;
import org.snapscript.core.Module;
import org.snapscript.core.Scope;

public class SystemContext {

   private final Context context;
   
   public SystemContext(Context context) {
      this.context = context;
   }

   public <T> T eval(Scope scope, String source) throws Exception {
      ExpressionEvaluator executor = context.getEvaluator();
      Module module = scope.getModule();
      String name = module.getName();
      
      return executor.evaluate(scope, source, name);
   }
   
   public void print(Scope scope, Object value)  throws Exception{
      System.out.print(value);
   }
   
   public void println(Scope scope, Object value) throws Exception{
      System.out.println(value);
   }
   
   public void printf(Scope scope, Object value, Object... values)  throws Exception{
      String text = String.valueOf(value);
      String result = String.format(text, values);
      
      System.out.print(result);
   }
   
   public void sleep(Scope scope, long time) throws Exception {
      Thread.sleep(time);
   }

   public long time(Scope scope) throws Exception {
      return System.currentTimeMillis();
   }
}
