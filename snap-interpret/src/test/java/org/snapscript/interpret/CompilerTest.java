package org.snapscript.interpret;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.LinkedHashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.snapscript.assemble.InstructionResolver;
import org.snapscript.assemble.ScriptCompiler;
import org.snapscript.assemble.ScriptContext;
import org.snapscript.core.Compiler;
import org.snapscript.core.Context;
import org.snapscript.core.Executable;
import org.snapscript.core.MapModel;
import org.snapscript.core.Model;
import org.snapscript.interpret.InterpretationResolver;

public class CompilerTest extends TestCase{
   public static void main(String[] l)throws Exception{
      new CompilerTest().testCompilerPerformance();
   }
   public void testCompilerPerformance() throws Exception {
      InstructionResolver set = new InterpretationResolver();
      Context c =new ScriptContext(set);
      Compiler compiler = new ScriptCompiler(c);
      
      compileScripts(compiler);
   }
   private String load(File file) throws Exception{      
      FileInputStream in = new FileInputStream(file);
      ByteArrayOutputStream out=new ByteArrayOutputStream();
      byte[] buffer=new byte[1024];
      int count = 0;
      while((count=in.read(buffer))!=-1){
         out.write(buffer,0,count);
      }
      return out.toString();
   }
   
   private void compileScripts(Compiler compiler) throws Exception {
      int iterations = 1000;
      for(int i = 0; i < 100;i++) {
         File file = new File("c:\\Work\\development\\github\\snapscript\\snap-interpret\\src\\test\\java\\org\\snapscript\\parse\\script"+i+".js");  
         if(file.exists()) {
            try {
               String source=load(file);
               long start=System.currentTimeMillis();
               long last=start;
               for(int j=0;j<iterations;j++){
                  compiler.compile(source);
                  last=System.currentTimeMillis();
               }
               long finish=System.currentTimeMillis();
               long duration=finish-start;
               long once=finish-last;
               
               System.err.println("Time taken to COMPILE ["+file.getName()+"] " +iterations+" times was " + duration + " last was "+once);
            }catch(Exception e){
               System.err.println(e);
            }
         }
      }
   }   
   public void testCompiler() throws Exception{
      InstructionResolver set = new InterpretationResolver();
      Context context =new ScriptContext(set);
      Compiler compiler = new ScriptCompiler(context);
      Executable executable = compiler.compile("var x=\"xx\";x.toString();");
      Map<String,Object>map=new LinkedHashMap<String, Object>();
      Model model = new MapModel(map);
      executable.execute(model);     
   }
   public void testCompilerWithArgument() throws Exception{
      InstructionResolver set = new InterpretationResolver();
      Context context =new ScriptContext(set);
      Compiler compiler = new ScriptCompiler(context);
      Executable executable = compiler.compile("map.put('y',x.substring(1));");
      Map<String,Object>map=new LinkedHashMap<String, Object>();
      Model model = new MapModel(map);
      map.put("map", map);
      map.put("x", "blah");
      executable.execute(model);
      assertEquals(map.get("y"), "lah");
   }
   public void testImportStatic() throws Exception{
      InstructionResolver set = new InterpretationResolver();
      Context context =new ScriptContext(set);
      Compiler compiler = new ScriptCompiler(context);
      Executable executable = compiler.compile("import static lang.Math.*;var x = 1.6; var y = round(x); map.put('x',x); map.put('y',y);");
      Map<String,Object>map=new LinkedHashMap<String, Object>();
      Model model = new MapModel(map);
      map.put("map", map);
      executable.execute(model);
      assertEquals(map.get("x"), 1.6d);
      assertEquals(map.get("y"), 2l);      
   }
   public void testImport() throws Exception{
      InstructionResolver set = new InterpretationResolver();
      Context context =new ScriptContext(set);
      Compiler compiler = new ScriptCompiler(context);
      Executable executable = compiler.compile("import security.SecureRandom; var rand = new SecureRandom(); var val = rand.nextInt(10); map.put('rand', rand); map.put('val', val);");
      Map<String,Object>map=new LinkedHashMap<String, Object>();
      Model model = new MapModel(map);
      map.put("map", map);
      executable.execute(model);
      assertEquals(map.get("rand").getClass(), java.security.SecureRandom.class);
      assertEquals(map.get("val").getClass(), Integer.class);      
   }
   public void testTypeConstraint() throws Exception{
      InstructionResolver set = new InterpretationResolver();
      Context context =new ScriptContext(set);
      Compiler compiler = new ScriptCompiler(context);
      Executable executable = compiler.compile("var num : Number = 1.0d; var decimal : Double = 5*num; map.put('num', num); map.put('decimal', decimal);");
      Map<String,Object>map=new LinkedHashMap<String, Object>();
      Model model = new MapModel(map);
      map.put("map", map);
      executable.execute(model);
      assertEquals(map.get("num"), 1.0);
      assertEquals(map.get("decimal"), 5.0);      
   }
   public void testTypeConstraintFailure() throws Exception{
      InstructionResolver set = new InterpretationResolver();
      Context context =new ScriptContext(set);
      Compiler compiler = new ScriptCompiler(context);
      Executable executable = compiler.compile("var num : Number = 1.0d; map.put('num',num); var decimal : String = 5*num;");
      Map<String,Object>map=new LinkedHashMap<String, Object>();
      Model model = new MapModel(map); 
      map.put("map", map);
      boolean failure=false;
      try {  
         executable.execute(model);
      }catch(Exception e){
         failure=true;
         e.printStackTrace();
      }
      assertEquals(map.get("num"), 1.0);
      assertTrue(failure);
   }
   public void testParameterTypeConstraint() throws Exception{
      InstructionResolver set = new InterpretationResolver();
      Context context =new ScriptContext(set);
      Compiler compiler = new ScriptCompiler(context);
      Executable executable = compiler.compile("function fun(x:String){return \"done=\"+x;}var y =fun(\"x\");map.put('y',y);");
      Map<String,Object>map=new LinkedHashMap<String, Object>();
      Model model = new MapModel(map);
      map.put("map", map);
      executable.execute(model);
      assertEquals(map.get("y"), "done=x");
   }
   public void testParameterTypeConstraintFailure() throws Exception{
      InstructionResolver set = new InterpretationResolver();
      Context context =new ScriptContext(set);
      Compiler compiler = new ScriptCompiler(context);
      Executable executable = compiler.compile("function fun(x:Date){return \"done=\"+x;}var y =fun(11.2);");
      Map<String,Object>map=new LinkedHashMap<String, Object>();
      Model model = new MapModel(map);
      boolean failure=false;
      try {  
         executable.execute(model);
      }catch(Exception e){
         e.printStackTrace();
         failure=true;
      }
      assertTrue(failure);
   }
}