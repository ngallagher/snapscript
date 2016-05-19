package org.snapscript.compile.instruction;

import org.snapscript.compile.instruction.construct.MapEntryList;
import org.snapscript.compile.instruction.define.TypeName;
import org.snapscript.core.Evaluation;
import org.snapscript.core.Scope;
import org.snapscript.core.Value;
import org.snapscript.core.ValueType;

public class AnnotationDeclaration implements Evaluation {

   private final MapEntryList list;
   private final TypeName name;
   
   public AnnotationDeclaration(TypeName name) {
      this(name, null);
   }
   
   public AnnotationDeclaration(TypeName name, MapEntryList list) {
      this.list = list;
      this.name = name;
   }

   @Override
   public Value evaluate(Scope scope, Object left) throws Exception {
      if(list != null) {
         return list.evaluate(scope, left);
      }
      return ValueType.getTransient(null);
   }
}
