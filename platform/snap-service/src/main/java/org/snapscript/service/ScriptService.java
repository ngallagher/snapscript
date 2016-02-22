package org.snapscript.service;

import org.snapscript.compile.Compiler;
import org.snapscript.compile.Executable;
import org.snapscript.compile.ResourceCompiler;
import org.snapscript.compile.StoreContext;
import org.snapscript.core.Context;
import org.snapscript.core.ExpressionEvaluator;
import org.snapscript.core.Model;
import org.snapscript.core.store.Store;

public class ScriptService {

   public static void main(String[] options) throws Exception {
      CommandLineParser parser = new CommandLineParser();
      CommandLine line = parser.parse(options);
      Store store = line.getStore();
      String evaluate = line.getEvaluation();
      String script = line.getScript();
      Model model = line.getModel();
      
      if(evaluate != null && script != null) {
         System.err.println("Both --evaluate and --script have been specified");
         System.exit(0);
      }
      if(evaluate == null && script == null) {
         System.err.println("Neither --evaluate or --script have been specified");
         System.exit(0);
      }
      Context context = new StoreContext(store);
      Compiler compiler = new ResourceCompiler(context);
      Executable executable = null;
      
      if(script != null) {
         executable = compiler.compile(script);
      }
      if(evaluate != null) {
         ExpressionEvaluator evaluator = context.getEvaluator();
         evaluator.evaluate(model, evaluate);
      } else {
         executable.execute(model);
      }
   }
}
