package org.snapscript.compile.instruction;

import java.lang.reflect.Modifier;
import java.util.List;

import org.snapscript.core.Function;
import org.snapscript.core.Module;
import org.snapscript.core.Result;
import org.snapscript.core.Scope;
import org.snapscript.core.Signature;
import org.snapscript.core.Statement;
import org.snapscript.core.Type;

public class ImportStatic extends Statement {   
   
   private final String location;
   private final String target;     
   
   public ImportStatic(Qualifier qualifier) {
      this.location = qualifier.getLocation();
      this.target = qualifier.getTarget();
   }
   
   @Override
   public Result compile(Scope scope) throws Exception {
      Module module = scope.getModule();
      Type type = module.addType(location);
      List<Function> methods = type.getFunctions();
      List<Function> funs = module.getFunctions();
      
      for(Function method : methods){
         Signature signature = method.getSignature();
         int modifiers = signature.getModifiers();
         
         if(Modifier.isStatic(modifiers)&&Modifier.isPublic(modifiers)){
            String name = method.getName();
            
            if(target == null || target.equals(name)) {
               funs.add(method);
            }
         }
      }
      return new Result();
   }
}
