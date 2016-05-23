package org.snapscript.compile.instruction;

import org.snapscript.compile.instruction.construct.MapEntryList;
import org.snapscript.core.Evaluation;
import org.snapscript.core.Scope;
import org.snapscript.core.Value;
import org.snapscript.core.ValueType;

public class AnnotationDeclaration implements Evaluation {

   private final AnnotationName name;
   private final MapEntryList list;
   
   public AnnotationDeclaration(AnnotationName name) {
      this(name, null);
   }
   
   public AnnotationDeclaration(AnnotationName name, MapEntryList list) {
      this.list = list;
      this.name = name;
   }

   @Override
   public Value evaluate(Scope scope, Object left) throws Exception {
      Value value = name.evaluate(scope, left);
      String name = value.getString();
      
      if(list != null) {
         return list.evaluate(scope, left);
      }
      return ValueType.getTransient(null);
   }
}
