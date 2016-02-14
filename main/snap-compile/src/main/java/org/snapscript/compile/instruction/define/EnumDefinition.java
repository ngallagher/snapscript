package org.snapscript.compile.instruction.define;

import java.util.concurrent.atomic.AtomicBoolean;

import org.snapscript.core.Initializer;
import org.snapscript.core.Result;
import org.snapscript.core.ResultType;
import org.snapscript.core.Scope;
import org.snapscript.core.Statement;
import org.snapscript.core.Type;

public class EnumDefinition extends Statement {

   private final DefaultConstructor constructor;
   private final AtomicBoolean define;
   private final EnumBuilder builder;
   private final EnumList list;
   private final TypePart[] parts;
   
   public EnumDefinition(TypeName name, TypeHierarchy hierarchy, EnumList list, TypePart... parts) {
      this.builder = new EnumBuilder(name, hierarchy);
      this.constructor = new DefaultConstructor(true);
      this.define = new AtomicBoolean(true);
      this.parts = parts;
      this.list = list;
   }

   @Override
   public Result compile(Scope scope) throws Exception {
      StaticScope other = new StaticScope(scope);
      InitializerCollector collector = new InitializerCollector();
      
      if(!define.compareAndSet(false, true)) {
         Type type = builder.create(other);
         Initializer keys = list.define(other, collector, type);
   
         for(TypePart part : parts) {
            Initializer initializer = part.define(other, collector, type);
            collector.update(initializer);
         }  
         constructor.define(other, collector, type); 
         keys.execute(other, type);
         collector.compile(other, type); 
         
         return ResultType.getNormal(type);
      }
      return ResultType.getNormal();
   }
}
