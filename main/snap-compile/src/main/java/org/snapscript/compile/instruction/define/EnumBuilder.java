package org.snapscript.compile.instruction.define;

import java.util.ArrayList;
import java.util.List;

import org.snapscript.core.Scope;
import org.snapscript.core.Type;

public class EnumBuilder {

   private final EnumConstantInitializer builder;
   private final TypeHierarchy hierarchy;
   private final TypeName name;
   private final List values;
   
   public EnumBuilder(TypeName name, TypeHierarchy hierarchy) {
      this.builder = new EnumConstantInitializer();
      this.values = new ArrayList();
      this.hierarchy = hierarchy;
      this.name = name;
   }
   
   public Type create(Scope scope) throws Exception {
      Type type = name.getType(scope);
      
      hierarchy.update(scope, type); 
      builder.declare(scope, type, values);
      
      return type;
   }
}
