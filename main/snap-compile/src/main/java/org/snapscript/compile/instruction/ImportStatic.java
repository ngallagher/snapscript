package org.snapscript.compile.instruction;

import java.util.List;

import org.snapscript.core.Function;
import org.snapscript.core.ModifierType;
import org.snapscript.core.Module;
import org.snapscript.core.Result;
import org.snapscript.core.ResultType;
import org.snapscript.core.Scope;
import org.snapscript.core.Statement;
import org.snapscript.core.Type;

public class ImportStatic extends Statement {   
   
   private final Qualifier qualifier;    
   
   public ImportStatic(Qualifier qualifier) {
      this.qualifier = qualifier;
   }
   
   @Override
   public Result compile(Scope scope) throws Exception {
      Module module = scope.getModule();
      String location = qualifier.getLocation();
      String target = qualifier.getTarget();
      Type type = module.addType(location);
      List<Function> methods = type.getFunctions();
      List<Function> functions = module.getFunctions();
      
      for(Function method : methods){
         int modifiers = method.getModifiers();
         
         if(ModifierType.isStatic(modifiers) && ModifierType.isPublic(modifiers)){
            String name = method.getName();
            
            if(target == null || target.equals(name)) {
               functions.add(method);
            }
         }
      }
      return ResultType.getNormal();
   }
}
