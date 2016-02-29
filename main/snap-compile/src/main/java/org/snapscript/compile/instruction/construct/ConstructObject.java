package org.snapscript.compile.instruction.construct;

import org.snapscript.compile.instruction.ArgumentList;
import org.snapscript.core.Compilation;
import org.snapscript.core.Context;
import org.snapscript.core.Evaluation;
import org.snapscript.core.Module;
import org.snapscript.core.Trace;
import org.snapscript.core.TraceInterceptor;
import org.snapscript.core.TraceEvaluation;
import org.snapscript.core.TraceType;

public class ConstructObject implements Compilation {
   
   private final Evaluation construct;
   
   public ConstructObject(Evaluation type) {
      this(type, null);         
   }
   
   public ConstructObject(Evaluation type, ArgumentList list) {
      this.construct = new CreateObject(type, list);
   } 
   
   @Override
   public Evaluation compile(Module module, int line) throws Exception {
      Context context = module.getContext();
      TraceInterceptor interceptor = context.getInterceptor();
      Trace trace = TraceType.getConstruct(module, line);
      
      return new TraceEvaluation(interceptor, construct, trace);
   }

}