package org.snapscript.compile.instruction.define;

import java.util.concurrent.atomic.AtomicBoolean;

import org.snapscript.core.Initializer;
import org.snapscript.core.Result;
import org.snapscript.core.ResultType;
import org.snapscript.core.Scope;
import org.snapscript.core.Type;

public class StaticConstantInitializer extends Initializer {

   private final StaticConstantCollector collector;
   private final AtomicBoolean done;
   
   public StaticConstantInitializer() {
      this.collector = new StaticConstantCollector();
      this.done = new AtomicBoolean(false);
   }

   @Override
   public Result compile(Scope scope, Type type) throws Exception { 
      if(done.compareAndSet(false, true)) {
         collector.collect(type);
      }
      return ResultType.getNormal();
   }
}
