package org.snapscript.core.export;

import org.snapscript.core.Context;
import org.snapscript.core.ExpressionExecutor;
import org.snapscript.core.Module;
import org.snapscript.core.Scope;

public class SystemContext {

   private final Context context;
   
   public SystemContext(Context context) {
      this.context = context;
   }

   public <T> T eval(Scope scope, String source) throws Exception {
      ExpressionExecutor executor = context.getExecutor();
      Module module = scope.getModule();
      String name = module.getName();
      
      return executor.execute(scope, name, source);
   }
   
   public void sleep(Scope scope, long time) throws Exception {
      Thread.sleep(time);
   }
   
   public void println(Scope scope, Object value) throws Exception{
      System.out.println(value);
   }
   
   public void print(Scope scope, Object value)  throws Exception{
      System.out.print(value);
   }

   public long time(Scope scope) throws Exception {
      return System.currentTimeMillis();
   }
}
