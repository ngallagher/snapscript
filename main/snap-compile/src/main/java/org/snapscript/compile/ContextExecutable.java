package org.snapscript.compile;

import org.snapscript.core.Context;
import org.snapscript.core.EmptyModel;
import org.snapscript.core.Model;
import org.snapscript.core.Package;
import org.snapscript.core.Scope;
import org.snapscript.core.ScopeMerger;
import org.snapscript.core.Statement;
import org.snapscript.core.error.ErrorHandler;
import org.snapscript.core.validate.ContextValidator;

public class ContextExecutable implements Executable{
   
   private final ContextValidator validator;
   private final ScopeMerger merger;
   private final Package library;
   private final Context context;
   private final Model model;
   private final String name;
   
   public ContextExecutable(Context context, Package library, String name){
      this.validator = new ContextValidator();
      this.merger = new ScopeMerger(context);
      this.model = new EmptyModel();
      this.library = library;
      this.context = context;
      this.name = name;
   }
   
   @Override
   public void execute() throws Exception {
      execute(model);
   }
   
   @Override
   public void execute(Model model) throws Exception{ 
      Scope scope = merger.merge(model, name);
      Statement script = library.compile(scope);
      ErrorHandler handler = context.getHandler();
      
      try {
         validator.validate(context);
         script.execute(scope);
      } catch(Throwable cause) {
         handler.throwExternal(scope, cause);
      }
   }

}
