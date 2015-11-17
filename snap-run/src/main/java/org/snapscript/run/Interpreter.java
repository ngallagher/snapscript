package org.snapscript.run;

import org.snapscript.compile.Compiler;
import org.snapscript.compile.ClassPathContext;
import org.snapscript.compile.StringCompiler;
import org.snapscript.core.Context;
import org.snapscript.core.EmptyModel;
import org.snapscript.core.Executable;
import org.snapscript.core.Model;
import org.snapscript.core.resource.ClassPathReader;
import org.snapscript.core.resource.ResourceReader;

public class Interpreter {
   
   private final SourceLoader loader;
   private final Model model;
   
   public Interpreter() {
      this.loader = new SourceLoader();
      this.model = new EmptyModel();
   }
   
   public void interpret(String script) {
      String source = null;
      
      try {
         source = loader.load(script);
      }catch(Exception e) {
         throw new IllegalStateException("Could not load script '" + script+ "'", e);
      }
      ResourceReader reader = new ClassPathReader();
      Context context = new ClassPathContext(model);
      Compiler compiler = new StringCompiler(context);
      
      try {
         Executable executable = compiler.compile(source);
         executable.execute();
      } catch(Exception e) {
         throw new IllegalStateException("Could not execute script '" + script +"':\n" + source, e);
      }
   }
   
   /**
    * java -classpath a;b;c;d -jar snap.jar "script.snap"
    */
   public static void main(String[] list) throws Exception {
      if(list.length != 1) {
         System.err.println("Script name required");
         System.exit(0);
      }
      Interpreter interpreter = new Interpreter();
      interpreter.interpret(list[0]);
   }
}
