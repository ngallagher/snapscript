package org.snapscript.run;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.snapscript.compile.ClassPathContext;
import org.snapscript.compile.Compiler;
import org.snapscript.compile.Executable;
import org.snapscript.compile.StringCompiler;
import org.snapscript.core.Context;
import org.snapscript.core.MapModel;
import org.snapscript.core.Model;

public class Interpreter {
   
   private static final String ARGUMENTS = "arguments";
   
   private final SourceLoader loader;
   private final String[] arguments;
   
   public Interpreter(String[] arguments) {
      this.loader = new SourceLoader();
      this.arguments = arguments;
   }
   
   public void interpret(String script) {
      Map<String, Object> map = Collections.<String, Object>singletonMap(ARGUMENTS, arguments);
      String source = null;
      
      try {
         source = loader.load(script);
      }catch(Exception e) {
         throw new IllegalStateException("Could not load script '" + script+ "'", e);
      }
      Context context = new ClassPathContext();
      Compiler compiler = new StringCompiler(context);
      Model model = new MapModel(map);
      
      try {
         Executable executable = compiler.compile(source);
         executable.execute(model);
      } catch(Exception e) {
         throw new IllegalStateException("Could not execute script '" + script +"':\n" + source, e);
      }
   }
   
   /**
    * java -classpath a;b;c;d -jar snap.jar "script.snap"
    */
   public static void main(String[] list) throws Exception {
      if(list.length < 1) {
         System.err.println("Script name required");
         System.exit(0);
      }
      String[] arguments = new String[list.length - 1];
      Interpreter interpreter = new Interpreter(arguments);
      
      for(int i = 1; i < list.length; i++) {
         arguments[i-1] = list[i];
      }
      interpreter.interpret(list[0]);
   }
}
