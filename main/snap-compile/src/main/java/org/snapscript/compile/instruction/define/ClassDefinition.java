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

public class ClassDefinition extends Statement {   
   
   private final FunctionPropertyGenerator generator;
   private final DefaultConstructor constructor;
   private final AtomicBoolean compile;
   private final ClassBuilder builder;
   private final TypePart[] parts;
   private final TypeName name;
   
   public ClassDefinition(AnnotationList annotations, TypeName name, TypeHierarchy hierarchy, TypePart... parts) {
      this.builder = new ClassBuilder(annotations, name, hierarchy);
      this.generator = new FunctionPropertyGenerator(); 
      this.constructor = new DefaultConstructor();
      this.compile = new AtomicBoolean(true);
      this.parts = parts;
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
      
      if(!compile.compareAndSet(false, true)) {
         Type type = builder.create(other);
         
         for(TypePart part : parts) {
            Initializer initializer = part.compile(other, collector, type);
            collector.update(initializer);
         } 
         constructor.compile(other, collector, type);
         generator.generate(type);
         
         return ResultType.getNormal(type);
      }
      return ResultType.getNormal();
   }

}
