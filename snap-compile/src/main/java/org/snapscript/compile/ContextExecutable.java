package org.snapscript.compile;

import org.snapscript.core.Context;
import org.snapscript.core.EmptyModel;
import org.snapscript.core.Model;
import org.snapscript.core.Package;
import org.snapscript.core.Scope;
import org.snapscript.core.ScopeMerger;
import org.snapscript.core.Statement;

public class ContextExecutable implements Executable{
   
   private final ScopeMerger merger;
   private final Package library;
   private final Model model;
 
   public ContextExecutable(Context context, Package library){
      this.merger = new ScopeMerger(context);
      this.model = new EmptyModel();
      this.library = library;
   }
   
   @Override
   public void execute() throws Exception {
      execute(model);
   }
   
   @Override
   public void execute(Model model) throws Exception{ 
      Scope scope = merger.merge(model);
      Statement script = library.compile(scope);
      
      script.execute(scope);
   }

}
