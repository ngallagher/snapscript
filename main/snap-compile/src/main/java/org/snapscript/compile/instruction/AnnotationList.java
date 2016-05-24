package org.snapscript.compile.instruction;

import java.util.List;

import org.snapscript.core.Annotation;
import org.snapscript.core.Function;
import org.snapscript.core.Module;
import org.snapscript.core.Parameter;
import org.snapscript.core.Property;
import org.snapscript.core.Scope;
import org.snapscript.core.Type;
import org.snapscript.core.Value;

public class AnnotationList {
   
   private final AnnotationDeclaration[] list;

   public AnnotationList(AnnotationDeclaration... list) {
      this.list = list;
   }

   public void apply(Scope scope, Module module) throws Exception {
      List<Annotation> annotations = module.getAnnotations();
      
      for(AnnotationDeclaration entry : list) {
         Value value = entry.evaluate(scope, null);
         Annotation annotation = value.getValue();
         
         annotations.add(annotation);
      }
   }
   
   public void apply(Scope scope, Type type) throws Exception {
      List<Annotation> annotations = type.getAnnotations();
      
      for(AnnotationDeclaration entry : list) {
         Value value = entry.evaluate(scope, null);
         Annotation annotation = value.getValue();
         
         annotations.add(annotation);
      }
   }
   
   public void apply(Scope scope, Property property) throws Exception {
      List<Annotation> annotations = property.getAnnotations();
      
      for(AnnotationDeclaration entry : list) {
         Value value = entry.evaluate(scope, null);
         Annotation annotation = value.getValue();
         
         annotations.add(annotation);
      }
   }
   
   public void apply(Scope scope, Function function) throws Exception {
      List<Annotation> annotations = function.getAnnotations();
      
      for(AnnotationDeclaration entry : list) {
         Value value = entry.evaluate(scope, null);
         Annotation annotation = value.getValue();
         
         annotations.add(annotation);
      }
   }
   
   public void apply(Scope scope, Parameter parameter) throws Exception {
      List<Annotation> annotations = parameter.getAnnotations();
      
      for(AnnotationDeclaration entry : list) {
         Value value = entry.evaluate(scope, null);
         Annotation annotation = value.getValue();
         
         annotations.add(annotation);
      }
   }
}
