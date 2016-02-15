package org.snapscript.compile.instruction;

import static org.snapscript.core.Reserved.TYPE_CONSTRUCTOR;

import java.util.concurrent.Callable;

import org.snapscript.core.Context;
import org.snapscript.core.InternalStateException;
import org.snapscript.core.Result;
import org.snapscript.core.Type;
import org.snapscript.core.bind.FunctionBinder;
import org.snapscript.parse.Line;

public class InstructionBuilder {
   
   private final InstructionProcessor processor;
   private final Context context;

   public InstructionBuilder(Context context) {
      this.processor = new InstructionProcessor(context);
      this.context = context;
   }
   
   public Object create(Type type, Object[] arguments, Line line) throws Exception {
      return create(type, arguments, line, false);
   }

   public Object create(Type type, Object[] arguments, Line line, boolean trace) throws Exception {
      FunctionBinder binder = context.getBinder();
      Callable<Result> callable = binder.bind(null, type, TYPE_CONSTRUCTOR, arguments);
      
      if(callable == null) {
         throw new InternalStateException("No constructor for " + type + " at line " + line);
      }
      Result result = callable.call();
      Object value = result.getValue();
      
      return processor.process(value, line);
   }


}
