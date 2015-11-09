package org.snapscript.interpret.define;

import java.util.ArrayList;
import java.util.List;

import org.snapscript.core.Scope;
import org.snapscript.core.Statement;
import org.snapscript.core.Type;
import org.snapscript.core.Value;
import org.snapscript.interpret.CompoundStatement;

public class TypeHierarchy {
   
   private final TraitName[] traits; 
   private final TypeName type;

   public TypeHierarchy(TraitName... traits) {
      this(null, traits);     
   }
   
   public TypeHierarchy(TypeName type, TraitName... traits) {
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
         Statement s = new CompoundStatement();// do nothing
         Type t =scope.getModule().addType("Any"); // invent a type!!
        
         new DefaultConstructor().define(scope, s, t); // add the default no arg constructor!!
         types.add(t);
      }
      for(int i = 0; i < traits.length; i++) {
         Value value = traits[i].evaluate(scope, null);
         Type trait = value.getValue();
         
         types.add(trait);
      }
      return types;
   }

}
