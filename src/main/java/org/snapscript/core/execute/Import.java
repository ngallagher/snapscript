package org.snapscript.core.execute;

import org.snapscript.core.Context;
import org.snapscript.core.Module;
import org.snapscript.core.Scope;
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
         Library library = loader.addImport(location);
         
         if(library != null) {
            library.include(scope);
         }
      } else {
         Library library = loader.addType(location, target);
         
         if(library != null) {
            library.include(scope);
         }
      }
      return new Result();
   }
}