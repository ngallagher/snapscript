package org.snapscript.compile.instruction.define;

import org.snapscript.compile.instruction.AnnotationList;
import org.snapscript.compile.instruction.NameExtractor;
import org.snapscript.core.Context;
import org.snapscript.core.ImportManager;
import org.snapscript.core.Module;
import org.snapscript.core.ModuleRegistry;
import org.snapscript.core.Scope;

public class ModuleBuilder {

   private final AnnotationList annotations;
   private final NameExtractor extractor;
   
   public ModuleBuilder(AnnotationList annotations, ModuleName module) {
      this.extractor = new NameExtractor(module);
      this.annotations = annotations;
   }

   public Module create(Scope scope) throws Exception {
      String name = extractor.extract(scope);
      Module parent = scope.getModule();
      Module module = create(parent, name);
      ImportManager manager = module.getManager();
      String include = parent.getName();
      
      annotations.apply(scope, module);
      manager.addImport(include); // make outer classes accessible
      
      return module;
   }
   
   protected Module create(Module parent, String name) throws Exception {
      String path = parent.getPath();
      String prefix = parent.getName();
      Context context = parent.getContext();
      ImportManager manager = parent.getManager();
      ModuleRegistry registry = context.getRegistry();
      Module module = registry.addModule(prefix +"."+ name, path); // create module
      
      manager.addImports(module); // add parent imports
      manager.addImport(prefix, name); // make module accessible by name
      
      return module;
   }
}
