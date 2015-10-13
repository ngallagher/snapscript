package org.snapscript.interpret.console;

import java.io.File;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.snapscript.assemble.InstructionResolver;
import org.snapscript.assemble.ScriptCompiler;
import org.snapscript.assemble.ScriptContext;
import org.snapscript.core.Context;
import org.snapscript.interpret.InterpretationResolver;

public class ScriptLauncher implements Runnable {
   
   private final ConsoleWriter output;
   private final ConsoleWriter info;
   private final String source;
   private final File file;
   private final boolean fork;
   
   public ScriptLauncher(ConsoleWriter output, ConsoleWriter info, String source, File file) {
      this(output, info, source, file, true);
   }
   
   public ScriptLauncher(ConsoleWriter output, ConsoleWriter info, String source, File file, boolean fork) {
      this.output = output;
      this.source = source;
      this.fork = fork;
      this.info = info;
      this.file = file;
   }
   
   @Override
   public void run() {
      compile();
      long start = System.currentTimeMillis();
      
      if(fork) {
         fork();
      } else {
         execute();
      }
      long finish = System.currentTimeMillis();
      long duration = finish - start;
      info.log("Time taken to execute was " + duration + " ms");
   }
   
   private void fork() {
      try {
         String javaHome = System.getProperty("java.home");
         String javaBin = javaHome + File.separator + "bin" + File.separator + "java";
         String classpath = System.getProperty("java.class.path");
         String className = ScriptRunner.class.getCanonicalName();
         String scriptPath = file.getCanonicalPath();
         
         ProcessBuilder builder = new ProcessBuilder(javaBin, "-cp", classpath, className, scriptPath);
         builder.redirectErrorStream(true);
         Process process = builder.start();
         InputStream input = process.getInputStream();
         ConsoleReader reader = new ConsoleReader(input);
         String line = reader.readLine();
         
         while(line != null){
            output.log(line);
            line = reader.readLine();
         }
         process.waitFor();
      }catch(Exception e) {
         StringWriter w = new StringWriter();
         PrintWriter p = new PrintWriter(w);
         e.printStackTrace(p);
         p.flush();
         info.log(w.toString());
      }
   }
   
   private void execute() {
      try {
         ScriptRunner.run(file.getCanonicalPath());
      }catch(Exception e) {
         StringWriter w = new StringWriter();
         PrintWriter p = new PrintWriter(w);
         e.printStackTrace(p);
         p.flush();
         info.log(w.toString());
      }
   }
   
   private void compile() {
      try {
         InstructionResolver set = new InterpretationResolver();
         Context context =new ScriptContext(set);
         ScriptCompiler compiler = new ScriptCompiler(context);
         long start = System.currentTimeMillis();
         compiler.compile(source);
         long finish = System.currentTimeMillis();
         long duration = finish - start;
         info.log("Time taken to compile was " + duration + " ms, size was " + source.length());
      }catch(Exception e) {
         StringWriter w = new StringWriter();
         PrintWriter p = new PrintWriter(w);
         e.printStackTrace(p);
         p.flush();
         info.log(w.toString());
         throw new RuntimeException("Script does not compile", e);
      }
   }
}
