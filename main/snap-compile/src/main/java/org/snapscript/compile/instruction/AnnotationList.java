package org.snapscript.compile.instruction;

import java.util.ArrayList;
import java.util.List;

import org.snapscript.core.Annotation;
import org.snapscript.core.Evaluation;
import org.snapscript.core.Scope;
import org.snapscript.core.Value;
import org.snapscript.core.ValueType;

public class AnnotationList implements Evaluation {
   
   private final AnnotationDeclaration[] list;

   public AnnotationList(AnnotationDeclaration... list) {
      this.list = list;
   }
   
   @Override
   public Value evaluate(Scope scope, Object left) throws Exception {
      List<Annotation> result = new ArrayList<Annotation>();
      
      for(AnnotationDeclaration entry : list) {
         Value value = entry.evaluate(scope, null);
         Annotation annotation = value.getValue();
         
         result.add(annotation);
      }
      return ValueType.getTransient(result);
   }
}
