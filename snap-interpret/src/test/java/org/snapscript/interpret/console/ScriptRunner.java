package org.snapscript.interpret.console;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.snapscript.assemble.InstructionResolver;
import org.snapscript.assemble.ScriptCompiler;
import org.snapscript.assemble.ScriptContext;
import org.snapscript.core.Context;
import org.snapscript.core.Executable;
import org.snapscript.core.MapModel;
import org.snapscript.core.Model;
import org.snapscript.interpret.InterpretationResolver;

public class ScriptRunner {

   public static void main(String[] list) throws Exception {
      run(list[0]);
   }
   
   public static void run(String file) throws Exception {
      try {
         InstructionResolver set = new InterpretationResolver();
         Context context = new ScriptContext(set);
         ScriptCompiler compiler = new ScriptCompiler(context);
         Map<String, Object> map = new HashMap<String, Object>();
         Model model = new MapModel(map);
         String source = load(file);
         Executable executable = compiler.compile(source);
         executable.execute(model);
      } catch (Exception e) {
         StringWriter w = new StringWriter();
         PrintWriter p = new PrintWriter(w);
         e.printStackTrace(p);
         p.flush();
         System.err.println(w.toString());
      }
   }

   private static String load(String source) throws Exception {
      File file = new File(source);
      FileInputStream in = new FileInputStream(file);
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      try {
         byte[] buffer = new byte[1024];
         int count = 0;
         while ((count = in.read(buffer)) != -1) {
            out.write(buffer, 0, count);
         }
      } finally {
         in.close();
      }
      return out.toString();
   }

}
