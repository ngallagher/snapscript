package org.snapscript.code.closure;

import org.snapscript.code.function.ParameterDeclaration;
import org.snapscript.code.function.ParameterList;
import org.snapscript.core.Scope;
import org.snapscript.core.function.Signature;

public class ClosureParameterList {
   
   private final ParameterList multiple;
   private final ParameterList single;
   
   public ClosureParameterList() {
      this(null, null);
   }
  
   public ClosureParameterList(ParameterList multiple) {
      this(multiple, null);
   }
   
   public ClosureParameterList(ParameterDeclaration single) {
      this(null, single);
   }
   
   public ClosureParameterList(ParameterList multiple, ParameterDeclaration single) {
      this.single = new ParameterList(single);
      this.multiple = multiple;
   }
   
   public Signature create(Scope scope) throws Exception{
      if(multiple != null) {
         return multiple.create(scope);
      }
      return single.create(scope);
   }
}
