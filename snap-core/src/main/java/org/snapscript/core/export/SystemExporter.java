package org.snapscript.core.export;

import java.util.ArrayList;
import java.util.List;

import org.snapscript.core.Context;
import org.snapscript.core.Function;
import org.snapscript.core.Module;

public class SystemExporter {
   
   private final List<Function> functions;
   private final MethodExporter exporter;
   private final SystemContext runtime;
   
   public SystemExporter(Context context) {
      this.functions = new ArrayList<Function>();
      this.exporter = new MethodExporter(context);
      this.runtime = new SystemContext(context);
   }
   
   public synchronized void export(Module module){
      List<Function> available = module.getFunctions();
      
      if(functions.isEmpty()) {
         try {
            List<Function> list = exporter.export(runtime);
            
            for(Function function : list) {
               functions.add(function);
            }
         } catch(Exception e) {
            throw new IllegalStateException("Could not export runtime", e);
         }
      }
      available.addAll(functions);
   }

}
