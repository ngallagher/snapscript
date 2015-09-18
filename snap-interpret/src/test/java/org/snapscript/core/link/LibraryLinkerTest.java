package org.snapscript.core.link;

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
import org.snapscript.core.MapModel;
import org.snapscript.core.Model;
import org.snapscript.core.ResultFlow;
import org.snapscript.core.execute.InterpretationResolver;

public class LibraryLinkerTest extends TestCase {
   private static final int ITERATIONS = 1;
   public static void main(String[] l)throws Exception{
      new LibraryLinkerTest().testLinker();
   }
   public void testLinker() throws Exception {
      InstructionResolver set = new InterpretationResolver();
      Context c =new ScriptContext(set);
      ScriptCompiler compiler = new ScriptCompiler(c);
   
      executeScript(compiler, "script1.js");      
 /*     executeScript(compiler, "perf2.js");    
      executeScript(compiler, "perf3.js"); */   
   }
   public static Object executeScript(ScriptCompiler compiler, String source) throws Exception {
      File file = new File("c:\\Work\\development\\github\\snapscript\\snap-interpret\\src\\test\\java\\org\\snapscript\\core\\link\\"+source);
      String script = load(file);

      Map<String, Object> map = new HashMap<String, Object>();
      
      map.put("out", System.out);
      map.put("err", System.err);
      map.put("count", 100);

      //LexerBuilder.print(LexerBuilder.create(), script, "script");
      
      Model model = new MapModel(map);
      long start=System.currentTimeMillis();
      long last=start;
      for(int j=0;j<ITERATIONS;j++){
         last=System.currentTimeMillis();

         compiler.compile(script).execute(model);
      }
      long finish=System.currentTimeMillis();
      long duration=finish-start;
      long once=finish-last;
      
      System.err.println("Time taken to execute "+ITERATIONS+" times was " + duration + " last was "+once);
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
