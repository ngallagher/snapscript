package org.snapscript.compile.instruction.define;

import java.util.concurrent.atomic.AtomicBoolean;

import org.snapscript.compile.instruction.NameExtractor;
import org.snapscript.core.Bug;
import org.snapscript.core.Module;
import org.snapscript.core.Result;
import org.snapscript.core.ResultType;
import org.snapscript.core.Scope;
import org.snapscript.core.Statement;
import org.snapscript.core.Type;

@Bug("Not working - this is just a template")
public class AnnotationDefinition extends Statement {

   private final DefaultConstructor constructor;
   private final AnnotationBuilder builder;
   private final NameExtractor extractor;
   private final AtomicBoolean define;
   
   public AnnotationDefinition(TypeName name, TypeHierarchy hierarchy) {
      this.builder = new AnnotationBuilder(name, hierarchy);
      this.constructor = new DefaultConstructor(true);
      this.extractor = new NameExtractor(name);
      this.define = new AtomicBoolean(true);
   }
   
   @Override
   public Result define(Scope scope) throws Exception {
      Module module = scope.getModule();
      String name = extractor.extract(scope);
      Type type = module.addType(name);
      
      return ResultType.getNormal(type);
   }

   @Override
   public Result compile(Scope scope) throws Exception {
      StaticScope other = new StaticScope(scope);
      InitializerCollector collector = new InitializerCollector();
      
      if(!define.compareAndSet(false, true)) {
         Type type = builder.create(other);

         constructor.compile(other, collector, type); 
         collector.compile(other, type); 
         
         return ResultType.getNormal(type);
      }
      return ResultType.getNormal();
   }
}
