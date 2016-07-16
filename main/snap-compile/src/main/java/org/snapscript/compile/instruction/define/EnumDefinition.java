package org.snapscript.compile.instruction.define;

import java.util.concurrent.atomic.AtomicBoolean;

import org.snapscript.compile.instruction.AnnotationList;
import org.snapscript.core.Initializer;
import org.snapscript.core.Module;
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
   private final TypeName name;
   
   public EnumDefinition(AnnotationList annotations, TypeName name, TypeHierarchy hierarchy, EnumList list, TypePart... parts) {
      this.builder = new EnumBuilder(name, hierarchy);
      this.constructor = new DefaultConstructor(true);
      this.define = new AtomicBoolean(true);
      this.parts = parts;
      this.list = list;
      this.name = name;
   }
   
   @Override
   public Result define(Scope scope) throws Exception {
      Module module = scope.getModule();
      String alias = name.getName(scope);
      Type type = module.addType(alias);
      
      return ResultType.getNormal(type);
   }

   @Override
   public Result compile(Scope scope) throws Exception {
      StaticScope other = new StaticScope(scope);
      InitializerCollector collector = new InitializerCollector();
      
      if(!define.compareAndSet(false, true)) {
         Type type = builder.create(other);
         Initializer keys = list.compile(other, collector, type);
   
         for(TypePart part : parts) {
            Initializer initializer = part.compile(other, collector, type);
            collector.update(initializer);
         }  
         constructor.compile(other, collector, type); 
         keys.execute(other, type);
         collector.compile(other, type); 
         
         return ResultType.getNormal(type);
      }
      return ResultType.getNormal();
   }
}
