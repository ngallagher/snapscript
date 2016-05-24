package org.snapscript.compile.instruction;

import org.snapscript.core.Evaluation;
import org.snapscript.core.InternalStateException;
import org.snapscript.core.Parameter;
import org.snapscript.core.Scope;
import org.snapscript.core.Type;

public class ParameterDeclaration {
   
   private NameExtractor extractor;
   private Constraint constraint;
   private Parameter parameter;
   private Modifier modifier;
   
   public ParameterDeclaration(AnnotationList list, Evaluation identifier){
      this(list, identifier, null, null);
   }
   
   public ParameterDeclaration(AnnotationList list, Evaluation identifier, Constraint constraint){
      this(list, identifier, null, constraint);
   }
   
   public ParameterDeclaration(AnnotationList list, Evaluation identifier, Modifier modifier){
      this(list, identifier, modifier, null);
   }
   
   public ParameterDeclaration(AnnotationList list, Evaluation identifier, Modifier modifier, Constraint constraint){
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
         Type type = constraint.create(scope);  
         
         if(type == null) {
            throw new InternalStateException("Constraint for '" +name + "' has not been imported");
         }
         return new Parameter(name, type, modifier != null);
      }
      return new Parameter(name, null, modifier != null);
   }
}  