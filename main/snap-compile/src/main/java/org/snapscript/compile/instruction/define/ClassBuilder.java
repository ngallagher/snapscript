package org.snapscript.compile.instruction.define;

import org.snapscript.compile.instruction.AnnotationList;
import org.snapscript.core.Scope;
import org.snapscript.core.Type;

public class ClassBuilder {   
   
   private final ClassConstantInitializer builder;
   private final AnnotationList annotations;
   private final TypeHierarchy hierarchy;
   private final TypeName name;
   
   public ClassBuilder(AnnotationList annotations, TypeName name, TypeHierarchy hierarchy) {
      this.builder = new ClassConstantInitializer();
      this.annotations = annotations;
      this.hierarchy = hierarchy;
      this.name = name;
   }
   
   public Type create(Scope scope) throws Exception {
      Type type = name.getType(scope);
      
      annotations.apply(scope, type);
      hierarchy.update(scope, type); 
      builder.declare(scope, type);
      
      return type;
   }
}
