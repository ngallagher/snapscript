package org.snapscript.compile.instruction;

import org.snapscript.core.Context;
import org.snapscript.core.Evaluation;
import org.snapscript.core.Module;
import org.snapscript.core.Package;
import org.snapscript.core.Result;
import org.snapscript.core.ResultType;
import org.snapscript.core.Scope;
import org.snapscript.core.Statement;
import org.snapscript.core.TypeLoader;
import org.snapscript.core.Value;

public class Import extends Statement {

   private final Qualifier qualifier;
   private final Evaluation alias;   
   
   public Import(Qualifier qualifier) {
      this(qualifier, null);
   }
   
   public Import(Qualifier qualifier, Evaluation alias) {
      this.qualifier = qualifier;
      this.alias = alias;
   }
   
   @Override
   public Result compile(Scope scope) throws Exception {
      Module module = scope.getModule();
      Context context = module.getContext();
      TypeLoader loader = context.getLoader();
      String location = qualifier.getLocation();
      String target = qualifier.getTarget();
      
      if(target == null) {
         Package library = loader.importPackage(location);
         
         if(library != null) {
            module.addImport(location);
            library.compile(scope);
         }
      } else {
         Package library = loader.importType(location, target);

         if(library != null) {
            if(alias != null) {
               Value value = alias.evaluate(scope, null);
               String alias = value.getString();
               
               module.addImport(location, target, alias);
            } else {
               module.addImport(location, target);
            }
            library.compile(scope);
         }
      }
      return ResultType.getNormal();
   }
}
