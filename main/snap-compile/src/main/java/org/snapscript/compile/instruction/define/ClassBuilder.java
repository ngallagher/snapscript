package org.snapscript.compile.instruction.define;

import org.snapscript.compile.instruction.AnnotationList;
import org.snapscript.compile.instruction.NameExtractor;
import org.snapscript.core.Module;
import org.snapscript.core.Scope;
import org.snapscript.core.Type;

public class ClassBuilder {   
   
   private final ClassConstantInitializer builder;
   private final AnnotationList annotations;
   private final TypeHierarchy hierarchy;
   private final NameExtractor extractor;
   
   public ClassBuilder(AnnotationList annotations, TypeName name, TypeHierarchy hierarchy) {
      this.builder = new ClassConstantInitializer();
      this.extractor = new NameExtractor(name);
      this.annotations = annotations;
      this.hierarchy = hierarchy;
   }
   
   public Type create(Scope scope) throws Exception {
      Module module = scope.getModule();
      String name = extractor.extract(scope);
      Type type = module.addType(name);
      
      annotations.apply(scope, type);
      hierarchy.update(scope, type); 
      builder.declare(scope, type);
      
      return type;
   }
}
