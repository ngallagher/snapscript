package org.snapscript.compile.instruction.define;

import java.util.ArrayList;
import java.util.List;

import org.snapscript.compile.instruction.NameExtractor;
import org.snapscript.core.Module;
import org.snapscript.core.Scope;
import org.snapscript.core.Type;

public class EnumBuilder {

   private final EnumConstantInitializer builder;
   private final TypeHierarchy hierarchy;
   private final NameExtractor extractor;
   private final List values;
   
   public EnumBuilder(TypeName name, TypeHierarchy hierarchy) {
      this.builder = new EnumConstantInitializer();
      this.extractor = new NameExtractor(name);
      this.values = new ArrayList();
      this.hierarchy = hierarchy;
   }
   
   public Type create(Scope scope) throws Exception {
      Module module = scope.getModule();
      String name = extractor.extract(scope);
      Type type = module.addType(name);
      
      hierarchy.update(scope, type); 
      builder.declare(scope, type, values);
      
      return type;
   }
}
