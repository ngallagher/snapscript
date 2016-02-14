package org.snapscript.compile.instruction;

import org.snapscript.core.Context;
import org.snapscript.core.Module;
import org.snapscript.core.Package;
import org.snapscript.core.Result;
import org.snapscript.core.ResultType;
import org.snapscript.core.Scope;
import org.snapscript.core.Statement;
import org.snapscript.core.TypeLoader;

public class Import extends Statement {

   private final String qualifier;
   private final String location;
   private final String target;     
   
   public Import(Qualifier qualifier) {
      this.qualifier = qualifier.getQualifier();
      this.location = qualifier.getLocation();
      this.target = qualifier.getTarget();
   }
   
   @Override
   public Result compile(Scope scope) throws Exception {
      Module module = scope.getModule();
      Context context = module.getContext();
      TypeLoader loader = context.getLoader();
      
      if(target == null) {
         Package library = loader.importPackage(location);
         
         if(library != null) {
            module.addImport(location);
            library.compile(scope);
         }
      } else {
         Package library = loader.importType(location, target);
         
         if(library != null) {
            module.addImport(location, target);
            library.compile(scope);
         }
      }
      return ResultType.getNormal();
   }
}
