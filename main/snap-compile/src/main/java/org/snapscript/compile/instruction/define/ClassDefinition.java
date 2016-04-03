package org.snapscript.compile.instruction.define;

import java.util.concurrent.atomic.AtomicBoolean;

import org.snapscript.core.Initializer;
import org.snapscript.core.Result;
import org.snapscript.core.ResultType;
import org.snapscript.core.Scope;
import org.snapscript.core.Statement;
import org.snapscript.core.Type;

public class ClassDefinition extends Statement {   
   
   private final FunctionPropertyGenerator generator;
   private final DefaultConstructor constructor;
   private final AtomicBoolean define;
   private final ClassBuilder builder;
   private final TypePart[] parts;
   
   public ClassDefinition(TypeName name, TypeHierarchy hierarchy, TypePart... parts) {
      this.generator = new FunctionPropertyGenerator(); 
      this.builder = new ClassBuilder(name, hierarchy);
      this.constructor = new DefaultConstructor();
      this.define = new AtomicBoolean(true);
      this.parts = parts;
   }

   @Override
   public Result compile(Scope scope) throws Exception {
      StaticScope other = new StaticScope(scope);
      InitializerCollector collector = new InitializerCollector();
      
      if(!define.compareAndSet(false, true)) {
         Type type = builder.create(other);
         
         for(TypePart part : parts) {
            Initializer initializer = part.define(other, collector, type);
            collector.update(initializer);
         } 
         constructor.define(other, collector, type);
         generator.generate(type);
         
         return ResultType.getNormal(type);
      }
      return ResultType.getNormal();
   }

}
