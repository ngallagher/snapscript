package org.snapscript.compile.instruction.template;

import java.io.Writer;

import org.snapscript.core.Context;
import org.snapscript.core.ExpressionExecutor;
import org.snapscript.core.Module;
import org.snapscript.core.Scope;

public class ExpressionSegment implements Segment {
   
   private String expression;
   private char[] source;
   private int off;
   private int length;
   
   public ExpressionSegment(char[] source, int off, int length) {
      this.expression = new String(source, off + 2, length - 3);
      this.source = source;
      this.length = length;
      this.off = off;         
   }
   
   @Override
   public void process(Scope scope, Writer writer) throws Exception {
      Module module = scope.getModule();
      String name = module.getName();
      Context context = module.getContext();
      ExpressionExecutor executor = context.getExecutor();
      Object value = executor.execute(scope, name, expression);
      
      if(value == null) {
         writer.write(source, off, length);
      } else {
         String text = String.valueOf(value);
         
         writer.append(text);            
      }
   }   
   
   @Override
   public String toString() {
      return expression;
   }
}