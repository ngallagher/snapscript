package org.snapscript.compile.instruction.define;

import java.util.ArrayList;
import java.util.List;

import org.snapscript.core.Result;
import org.snapscript.core.Scope;
import org.snapscript.core.Type;
import org.snapscript.core.Value;

public class TypeHierarchy {
   
   private final AnyDefinition definition;
   private final TraitName[] traits; 
   private final TypeName type;

   public TypeHierarchy(TraitName... traits) {
      this(null, traits);     
   }
   
   public TypeHierarchy(TypeName type, TraitName... traits) {
      this.definition = new AnyDefinition();
      this.traits = traits;
      this.type = type;
   }

   public List<Type> create(Scope scope) throws Exception {
      List<Type> types = new ArrayList<Type>();
      
      if(type != null) {
         Value value = type.evaluate(scope, null);
         Type base = value.getValue();
         
         types.add(base);
      }else {
         Result result = definition.compile(scope);
         Type type = result.getValue();
         
         types.add(type);
      }
      for(int i = 0; i < traits.length; i++) {
         Value value = traits[i].evaluate(scope, null);
         Type trait = value.getValue();
         
         types.add(trait);
      }
      return types;
   }

}
