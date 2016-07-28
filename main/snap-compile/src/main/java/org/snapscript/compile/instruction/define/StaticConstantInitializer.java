package org.snapscript.compile.instruction.define;

import org.snapscript.core.Type;

public class StaticConstantInitializer extends StaticInitializer {
   
   private final StaticConstantCollector collector;
   
   public StaticConstantInitializer() {
      this.collector = new StaticConstantCollector();
   }

   @Override
   protected void compile(Type type) throws Exception { 
      collector.collect(type);
   }
}
