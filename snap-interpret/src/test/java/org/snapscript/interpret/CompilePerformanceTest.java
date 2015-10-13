package org.snapscript.interpret;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.snapscript.assemble.InstructionResolver;
import org.snapscript.assemble.ScriptCompiler;
import org.snapscript.assemble.ScriptContext;
import org.snapscript.core.Context;
import org.snapscript.core.Executable;
import org.snapscript.core.MapModel;
import org.snapscript.core.Model;
import org.snapscript.core.ResultFlow;
import org.snapscript.interpret.InterpretationResolver;

//Assembly time  took 376
//Binary assemble time was 2004 normal was 376
//Time taken to compile  was 2989 size was 57071
public class CompilePerformanceTest extends TestCase {   

   private static final int ITERATIONS = 20;
   public static void main(String[] l)throws Exception{
      new CompilePerformanceTest().testCompilerPerformance();
   }
   public void testCompilerPerformance() throws Exception {
      //compileScript("perf4.js");  
      compileScript("perf4.js"); 
 /*     executeScript("perf2.js");    
      executeScript("perf3.js"); */   
   }
   public static void compileScript(String source) throws Exception {
      executeScript(source, false);
      
   }
   public static Object executeScript(String source) throws Exception {
      return executeScript(source, true);
   }
   public static Object executeScript(String source, boolean execute) throws Exception {
      File file = new File("c:\\Work\\development\\github\\snapscript\\snap-interpret\\src\\test\\java\\org\\snapscript\\interpret\\"+source);
      String script = load(file);

      for(int j=0;j<ITERATIONS;j++){
         long start=System.currentTimeMillis();
         InstructionResolver set = new InterpretationResolver();
         Context c =new ScriptContext(set);
         ScriptCompiler compiler = new ScriptCompiler(c);
         Map<String, Object> map = new HashMap<String, Object>();
         
         map.put("out", System.out);
         map.put("err", System.err);
         map.put("count", 100);
         
         Model model = new MapModel(map);
         Executable e=compiler.compile(script);
         long finish=System.currentTimeMillis();
         long duration=finish-start;
         System.err.println("Time taken to compile  was " + duration+" size was "+script.length());
         start=System.currentTimeMillis();
         if(execute){
            e.execute(model);
         }
         finish=System.currentTimeMillis();
         duration=finish-start;
         
         if(execute){
            System.err.println("Time taken to execute  was " + duration);
         }
      }
      return ResultFlow.NORMAL;
   }
   private static String load(File file) throws Exception{      
      FileInputStream in = new FileInputStream(file);
      ByteArrayOutputStream out=new ByteArrayOutputStream();
      byte[] buffer=new byte[1024];
      int count = 0;
      while((count=in.read(buffer))!=-1){
         out.write(buffer,0,count);
      }
      return out.toString();
   }  
}