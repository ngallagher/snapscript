package org.snapscript.interpret.console;

import java.io.File;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.Socket;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import org.snapscript.assemble.InstructionResolver;
import org.snapscript.assemble.ScriptCompiler;
import org.snapscript.assemble.ScriptContext;
import org.snapscript.core.Context;
import org.snapscript.interpret.InterpretationResolver;
import org.snapscript.parse.SyntaxCompiler;
import org.snapscript.parse.SyntaxParser;

public class ScriptLauncher implements ScriptTask, Runnable {
   
   private final AtomicReference<Socket> port;
   private final ConsoleWriter output;
   private final ConsoleWriter info;
   private final String source;
   private final File file;
   private final boolean fork;
   
   public ScriptLauncher(ConsoleWriter output, ConsoleWriter info, String source, File file) {
      this(output, info, source, file, true);
   }
   
   public ScriptLauncher(ConsoleWriter output, ConsoleWriter info, String source, File file, boolean fork) {
      this.port = new AtomicReference<Socket>();
      this.output = output;
      this.source = source;
      this.fork = fork;
      this.info = info;
      this.file = file;
   }
   
   public void stop() {
      try {
         Socket socket = port.get();
         
         if(socket != null) {
            socket.getOutputStream().write(1);
            socket.getOutputStream().close();
            port.set(null);
         }
      }catch(Exception e) {
         StringWriter w = new StringWriter();
         PrintWriter p = new PrintWriter(w);
         e.printStackTrace(p);
         p.flush();
         info.log(w.toString());
      }
   }
   
   @Override
   public void run() {
      compile();
      syntax();
      long start = System.nanoTime();
      
      if(fork) {
         fork();
      } else {
         execute();
      }
      long finish = System.nanoTime();
      long duration = finish - start;
      long millis = TimeUnit.NANOSECONDS.toMillis(duration);
      
      info.log("Time taken to execute was " + millis + " ms");
   }
   
   private void syntax(){
      try {
         SyntaxCompiler analyzer = new SyntaxCompiler();
         SyntaxParser parser = analyzer.compile();
         String syntax = SyntaxPrinter.print(parser, source, "script");
         info.log(syntax);
      }catch(Exception e){
         //ignore for now
      }
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
         
         if(!line.startsWith("port=")) {
            throw new IllegalStateException(ScriptRunner.class.getName() + " did not provide port");
         }
         String[] parts = line.split("=");
         port.set(new Socket("localhost", Integer.parseInt(parts[1]))); // set the listen port 
         line = reader.readLine();
         
         while(line != null){
            output.log(line);
            line = reader.readLine();
         }
         process.waitFor();
         line = reader.readLine();
         while(line != null){
            output.log(line);
            line = reader.readLine();
         }
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
         long start = System.nanoTime();
         compiler.compile(source);
         long finish = System.nanoTime();
         long duration = finish - start;
         long millis = TimeUnit.NANOSECONDS.toMillis(duration);
         info.log("Time taken to compile was " + millis + " ms, size was " + source.length());
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
