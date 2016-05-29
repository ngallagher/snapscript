package org.snapscript.core.extend;

import java.util.ArrayList;
import java.util.List;

import org.snapscript.core.Context;
import org.snapscript.core.Function;
import org.snapscript.core.InternalStateException;
import org.snapscript.core.Module;

public class ModuleExtender {
   
   private final List<Function> functions;
   private final FunctionExtractor exporter;
   private final ScopeExtension system;
   
   public ModuleExtender(Context context) {
      this.functions = new ArrayList<Function>();
      this.exporter = new FunctionExtractor(context);
      this.system = new ScopeExtension(context);
   }
   
   public synchronized void extend(Module module){
      List<Function> available = module.getFunctions();
      
      if(functions.isEmpty()) {
         try {
            List<Function> list = exporter.extract(system);
            
            for(Function function : list) {
               functions.add(function);
            }
         } catch(Exception e) {
            throw new InternalStateException("Could not export runtime", e);
         }
      }
      available.addAll(functions);
   }

}
