package org.snapscript.test;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.Reader;

import org.snapscript.run.Interpreter;

public class TestLauncher {

   public static void main(String[] list) throws Exception {
      try {
         String javaHome = System.getProperty("java.home");
         String javaBin = javaHome + File.separator + "bin" + File.separator + "java";
         String classpath = System.getProperty("java.class.path");
         String className = Interpreter.class.getCanonicalName();
         
         ProcessBuilder builder = new ProcessBuilder(javaBin, "-cp", classpath, className, "test_runner.snap");
         builder.redirectErrorStream(true);
         Process process = builder.start();
         InputStream input = process.getInputStream();
         ConsoleReader reader = new ConsoleReader(input);
         String line = reader.readLine();
         
         while(line != null){
            System.err.println(line);
            line = reader.readLine();
         }
         process.waitFor();
      }catch(Exception e) {
         e.printStackTrace();
      }
   }
   
   private static class ConsoleReader {

      private final LineNumberReader parser;
      private final StringBuilder builder;
      private final InputStream buffer;
      private final Reader reader;

      public ConsoleReader(InputStream source) {
         this.buffer = new BufferedInputStream(source);
         this.reader = new InputStreamReader(buffer);
         this.parser = new LineNumberReader(reader);
         this.builder = new StringBuilder();
      }

      public String readAll() throws IOException {
         while (true) {
            String line = parser.readLine();

            if (line != null) {
               builder.append("\r\n");
               builder.append(line);
            } else {
               break;
            }
         }
         return builder.toString();
      }

      public String readLine() throws IOException {
         return parser.readLine();
      }
   }
}
