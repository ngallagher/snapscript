package org.snapscript.interpret.define;

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
import org.snapscript.core.ContextModule;
import org.snapscript.core.Executable;
import org.snapscript.core.MapModel;
import org.snapscript.core.Model;
import org.snapscript.interpret.InterpretationResolver;

public class TestRunnerTest extends TestCase{
   private static final int ITERATIONS = 1;
   public static void main(String[] l)throws Exception{
      new TestRunnerTest().testScripts();
   }
   public void testScripts() throws Exception {
      executeScript(1);
      for(int i =0;i<100;i++){
         executeScript(i);
      }
   }
   public static void executeScript(int i) throws Exception {
      File file = new File("c:\\Work\\development\\github\\snapscript\\snap-interpret\\src\\test\\java\\org\\snapscript\\interpret\\define\\test"+i+".snap");
      if(file.exists()){
         try{
            String script = load(file);
            
            Map<String, Object> map = new HashMap<String, Object>();
            Model s = new MapModel(map);
            InstructionResolver set = new InterpretationResolver();
            Context context =new ScriptContext(set);
            ContextModule m = new ContextModule(context);
            ScriptCompiler compiler = new ScriptCompiler(context);
   
            
            map.put("out", System.out);
            map.put("err", System.err);
            map.put("count", 100);
   
            long start=System.currentTimeMillis();      
            
               
            Executable e=compiler.compile(script);
            long compile=System.currentTimeMillis()-start;
            e.execute(s);
            
            long finish=System.currentTimeMillis();
            long duration=finish-start;        
            
            System.err.println("script="+file.getName()+" compile-time="+compile+" execute-time="+duration);
         }catch(Exception e){
            throw new IllegalStateException("File: "+file+" error:"+e.getMessage(),e);
         }
      }
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
