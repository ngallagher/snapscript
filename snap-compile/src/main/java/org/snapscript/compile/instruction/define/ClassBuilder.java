package org.snapscript.compile.instruction.define;

import org.snapscript.compile.instruction.NameExtractor;
import org.snapscript.core.Module;
import org.snapscript.core.Scope;
import org.snapscript.core.Type;

public class ClassBuilder {   
   
   private final TypeHierarchy hierarchy;
   private final NameExtractor extractor;
   
   public ClassBuilder(TypeName name, TypeHierarchy hierarchy) {
      this.extractor = new NameExtractor(name);
      this.hierarchy = hierarchy;
   }
   
   public Type create(Scope scope) throws Exception {
      Module module = scope.getModule();
      String name = extractor.extract(scope);
      Type type = module.addType(name);
      
      if(hierarchy != null) {
         hierarchy.update(scope, type); 
      }
      return type;
   }
}
