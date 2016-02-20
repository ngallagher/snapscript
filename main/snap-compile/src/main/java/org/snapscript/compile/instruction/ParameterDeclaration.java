package org.snapscript.compile.instruction;

import org.snapscript.core.Evaluation;
import org.snapscript.core.InternalStateException;
import org.snapscript.core.Module;
import org.snapscript.core.Scope;
import org.snapscript.core.Type;
import org.snapscript.core.Value;

public class ParameterDeclaration {
   
   private NameExtractor extractor;
   private Constraint constraint;
   private Parameter parameter;
   private Modifier modifier;
   
   public ParameterDeclaration(Evaluation identifier){
      this(identifier, null, null);
   }
   
   public ParameterDeclaration(Evaluation identifier, Constraint constraint){
      this(identifier, null, constraint);
   }
   
   public ParameterDeclaration(Evaluation identifier, Modifier modifier){
      this(identifier, modifier, null);
   }
   
   public ParameterDeclaration(Evaluation identifier, Modifier modifier, Constraint constraint){
      this.extractor = new NameExtractor(identifier);
      this.constraint = constraint;
      this.modifier = modifier;
   }

   public Parameter get(Scope scope) throws Exception {
      if(parameter == null) {
         parameter = create(scope);
      }
      return parameter;
   }
   
   private Parameter create(Scope scope) throws Exception {
      String name = extractor.extract(scope);
      
      if(constraint != null && name != null) { 
         Value value = constraint.evaluate(scope, null);  
         String alias = value.getString();
         Module module = scope.getModule();
         Type type = module.getType(alias);
         
         if(type == null) {
            throw new InternalStateException("Constraint '" + alias + "' for '" +name + "' was not imported");
         }
         return new Parameter(name, type, modifier != null);
      }
      return new Parameter(name, null, modifier != null);
   }
}  