package org.snapscript.compile.instruction.define;

import java.util.List;

import org.snapscript.core.Result;
import org.snapscript.core.Scope;
import org.snapscript.core.Type;
import org.snapscript.core.Value;

public class TypeHierarchy {
   
   private final AnyDefinition definition;
   private final TraitName[] traits; 
   private final TypeName name;

   public TypeHierarchy(TraitName... traits) {
      this(null, traits);     
   }
   
   public TypeHierarchy(TypeName name, TraitName... traits) {
      this.definition = new AnyDefinition();
      this.traits = traits;
      this.name = name;
   }

   public void update(Scope scope, Type type) throws Exception {
      List<Type> types = type.getTypes();
      
      if(name != null) {
         Value value = name.evaluate(scope, null);
         Type base = value.getValue();
         
         types.add(base);
      }else {
         Result result = definition.compile(scope);
         Type base = result.getValue();
         
         types.add(base);
      }
      for(int i = 0; i < traits.length; i++) {
         Value value = traits[i].evaluate(scope, null);
         Type trait = value.getValue();
         
         types.add(trait);
      }
   }

}
