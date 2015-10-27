package org.snapscript.interpret;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.snapscript.assemble.Assembler;
import org.snapscript.assemble.InstructionResolver;
import org.snapscript.assemble.ScriptContext;
import org.snapscript.core.Context;
import org.snapscript.core.MapModel;
import org.snapscript.core.Model;
import org.snapscript.core.ModelScope;
import org.snapscript.core.ModuleScope;
import org.snapscript.core.ResultFlow;
import org.snapscript.core.Scope;
import org.snapscript.core.Statement;
import org.snapscript.interpret.Evaluation;
import org.snapscript.interpret.InterpretationResolver;
import org.snapscript.interpret.console.SyntaxPrinter;
import org.snapscript.parse.SyntaxCompiler;
import org.snapscript.parse.SyntaxNode;
import org.snapscript.parse.SyntaxParser;

public class EvaluationTest extends TestCase {   

   public void testEvaluations() throws Exception {
      Map<String, Object> model = new HashMap<String, Object>();
   
      
      model.put("out", System.out);
      model.put("err", System.err);
      model.put("date", new Date());
      model.put("str", new String("some string variable"));
      model.put("array", new String[]{"index-0","index-1","index-2"});
      model.put("list", Arrays.asList("index-0","index-1","index-2"));
      model.put("x", 12d);
      model.put("y", 5d);

      assertEquals(executeScript("script18.js", model), ResultFlow.NORMAL);
      assertEquals(executeScript("script14.js", model), ResultFlow.NORMAL);
      assertEquals(executeScript("script17.js", model), ResultFlow.NORMAL);
      assertEquals(executeScript("script16.js", model), ResultFlow.NORMAL);
      assertEquals(executeScript("script15.js", model), ResultFlow.NORMAL);
      //assertEquals(evaluate("x=12.0d", "expression", model), 12.0d);      
      assertEquals(evaluate("[]", "expression", model), Arrays.asList());
      assertEquals(evaluate("\"x\"+\"y\"", "expression", model), "xy");
      //assertEquals(evaluate("str=\"x\"+\"y\"", "expression", model), "xy");
      //assertEquals(evaluate("str", "expression", model), "xy");      
      //assertEquals(evaluate("str+=\"x\"+\"y\"", "expression", model), "xyxy");
      assertEquals(evaluate("str!=null", "expression", model), Boolean.TRUE);
      assertEquals(evaluate("null!=null", "expression", model), Boolean.FALSE);
      assertEquals(evaluate("null==null", "expression", model), Boolean.TRUE);       
      //assertEquals(evaluate("str=\"some string variable\"", "expression", model), "some string variable");     
      assertEquals(statement("{var result=\"\"; result+=\"shell result=\"+x+\",\";}", "statement", model), ResultFlow.NORMAL);
      //assertEquals(evaluate("result", "expression", model), "shell result=12.0,");      
      assertEquals(statement("{var tt=\"\";}", "statement", model), ResultFlow.NORMAL);      
      //assertEquals(evaluate("tt", "expression", model), "");
      assertEquals(statement("{var tt=\"\";tt+=\"a\";tt+=1;out.println(tt);}", "statement", model), ResultFlow.NORMAL);
     // assertEquals(evaluate("tt", "expression", model), "a1");
      assertEquals(evaluate("str+\"blah\"", "expression", model), "some string variableblah");
      //assertEquals(evaluate("str=\"some string variable\"", "expression", model), "some string variable");
      assertEquals(evaluate("\"\\\"\"", "expression", model),new String(new char[]{'"'}));
      assertEquals(evaluate("12.0d", "expression", model), 12.0d);
      assertEquals(evaluate("12.0f", "expression", model), 12.0f);
      assertEquals(evaluate("12", "expression", model), 12);
      assertEquals(evaluate("12L", "expression", model), 12l);      
      assertEquals(evaluate("\"hello world\"", "expression", model), "hello world");
      assertEquals(evaluate("\"string with a \\\" xxx\\\" string\"", "expression", model), "string with a \" xxx\" string");
      assertEquals(evaluate("x", "expression", model), model.get("x"));
      assertEquals(evaluate("date", "expression", model), model.get("date"));
      assertEquals(evaluate("str.toString()", "expression", model), model.get("str"));
      assertEquals(evaluate("str.substring(1)", "expression", model), "ome string variable");
      assertEquals(evaluate("str.substring(1, 3)", "expression", model), "om");
      assertEquals(evaluate("str.concat(str.substring(1, 3))", "expression", model), "some string variableom");
      assertEquals(evaluate("str.concat(str.substring(1, 3)).toString()", "expression", model), "some string variableom");
      assertEquals(evaluate("array[1]", "expression", model), "index-1");
      assertEquals(evaluate("array[2]", "expression", model), "index-2");
      assertEquals(evaluate("array[0]", "expression", model), "index-0");
      assertEquals(evaluate("array[0].concat(array[1].substring(2))", "expression", model), "index-0dex-1");
      assertEquals(evaluate("list[1]", "expression", model), "index-1");
      assertEquals(evaluate("list[2]", "expression", model), "index-2");
      assertEquals(evaluate("list[0]", "expression", model), "index-0");  
      assertEquals(evaluate("array[0].concat(array[1].substring(2))", "expression", model, 1000000), "index-0dex-1");
      assertEquals(evaluate("x+y", "expression", model), 17d);
      assertEquals(evaluate("x+y*2", "expression", model), 22d);
      assertEquals(evaluate("(x+y)*2", "expression", model), 34d);
      assertEquals(evaluate("x+(y*2)", "expression", model), 22d);
      assertEquals(evaluate("x+(y*str.length())", "expression", model), 112d);
      assertEquals(evaluate("x+(y*str.length())+1", "expression", model), 113d);
      assertEquals(evaluate("x+(y*str.length()+1)", "expression", model), 113d);
      assertEquals(evaluate("x+(y*str.length()+1)", "expression", model, 1000000), 113d);   
      assertEquals(evaluate("(x+y)*2", "expression", model, 1000000), 34d);
      assertEquals(evaluate("(x+y)*2", "expression", model, 1000000), 34d);
      assertEquals(evaluate("x>y", "expression", model), true);
      assertEquals(evaluate("x>=y", "expression", model), true); 
      assertEquals(evaluate("x==y", "expression", model), false); 
      assertEquals(evaluate("x<y", "expression", model), false);
      assertEquals(evaluate("x<y*100d", "expression", model), true);
      assertEquals(evaluate("x==y+(x-y)", "expression", model), true);
      assertEquals(evaluate("0xff>>>4==0x0f", "expression", model), true);
      assertEquals(evaluate("x>y&&y!=2d", "expression", model), true);    
      assertEquals(evaluate("x>y&&y!=2d&&y==y", "expression", model), true); 
      assertEquals(evaluate("x>y&&true", "expression", model), true);
      //assertEquals(evaluate("x+1==++x", "expression", model), true);
      //assertEquals(evaluate("x++", "expression", model), 13d);
      //assertEquals(evaluate("x++", "expression", model), 14d);
      //assertEquals(evaluate("++x", "expression", model), 16d);
      //assertEquals(evaluate("x=1d", "expression", model), 1d);
      //assertEquals(evaluate("x++", "expression", model), 1d);
      assertEquals(evaluate("x+1", "expression", model), 13d);
      //assertEquals(evaluate("y=x+2", "expression", model), 4d);
      assertEquals(evaluate("y", "expression", model), 5d);
      //assertEquals(evaluate("x=y*=2", "expression", model), 8d);
      assertEquals(evaluate("x", "expression", model), 12d);
      assertEquals(evaluate("[1, 2, 3]", "expression", model), Arrays.asList(1, 2, 3));
      assertEquals(evaluate("[1, 2, 3].size()", "expression", model), 3);
      
      Map map1 = new LinkedHashMap();
      map1.put("a", 1);
      map1.put("b", 2);
      map1.put("c", 3);
      assertEquals(evaluate("{a: 1, b: 2, c: 3}", "expression", model), map1);
      
      Map map2 = new LinkedHashMap();
      map2.put("a", 1);
      map2.put("b", 2);
      map2.put("c", 3);
      assertEquals(evaluate("{\"a\": 1, \"b\": 2, \"c\": 3}", "expression", model), map2);  
      
      Map map3 = new LinkedHashMap();
      map3.put(1, 1);
      map3.put(2, 2);
      map3.put(3, 3);
      assertEquals(evaluate("{1: 1, 2: 2, 3: 3}", "expression", model), map3); 
      
      assertEquals(evaluate("new String()", "expression", model), new String());
      assertEquals(evaluate("new String(\"some stuff\")", "expression", model), "some stuff");
      assertEquals(evaluate("new String(\"new str\")", "expression", model), "new str");
      //assertEquals(evaluate("str", "expression", model), "new str");
      assertEquals(statement("var r=1;", "statement", model), ResultFlow.NORMAL);
      //assertEquals(evaluate("r", "expression", model), 1);
      //assertEquals(statement("r=3;", "statement", model), ResultFlow.NORMAL);
      //assertEquals(evaluate("r", "expression", model), 3);
      assertEquals(statement("{var a=1;var b=2;b=a*10;a=12;b++;}", "compound-statement", model), ResultFlow.NORMAL);
      //assertEquals(evaluate("a", "expression", model), 12);
      //assertEquals(evaluate("b", "expression", model), 11d);
      //assertEquals(statement("if(a>2){a++;}", "statement", model), ResultFlow.NORMAL);
      //assertEquals(evaluate("a", "expression", model), 13d);
      //assertEquals(statement("if(a>20d){a++;}else{a--;}", "statement", model), ResultFlow.NORMAL);
      //assertEquals(evaluate("a", "expression", model), 12d);
      //assertEquals(statement("if(a>20d){a++;}else if(a==12d){a--;}else{a*=2;}", "statement", model), ResultFlow.NORMAL);
      //assertEquals(evaluate("a", "expression", model), 11d);
      //assertEquals(statement("if(a>20d){a++;}else if(a==12d){a--;}else{a*=2;}", "statement", model), ResultFlow.NORMAL);
      //assertEquals(evaluate("a", "expression", model), 22d);
      assertEquals(statement("break;", "statement", model), ResultFlow.BREAK);
      assertEquals(statement("continue;", "statement", model), ResultFlow.CONTINUE);
      //assertEquals(statement("while(a++<100000d){out.println(a);}", "statement", model), ResultFlow.NORMAL);
      //assertEquals(statement("while(a>0d){var sb=new StringBuilder();sb.append(\"a=\");sb.append(a--);out.println(sb);}", "statement", model), ResultFlow.NORMAL);
      assertEquals(statement("for(var i=0d;i<10d;i++){out.println(i);}", "statement", model), ResultFlow.NORMAL);
      assertEquals(statement("for(var i=0d;i<10d;i++){if(i==5d)break;out.println(i);}", "statement", model), ResultFlow.NORMAL);
      assertEquals(statement("for(var i=0d;i<10d;i++){if(i==5d)continue;out.println(i);}", "statement", model), ResultFlow.NORMAL);
      assertEquals(statement("for(var i in [2,4,6,8]){out.println(i);}", "statement", model), ResultFlow.NORMAL);
      assertEquals(statement("{var list=[1,2,3,4,5,77,88,99,111];for(var i in list)out.println(i);}", "statement", model), ResultFlow.NORMAL);
      assertEquals(statement("for(var i in {a:1,b:2,c:3}.entrySet()){out.println(i.getKey());}", "statement", model), ResultFlow.NORMAL);
      assertEquals(statement("for(var i in {a:1,b:2,c:3}){out.println(i.getKey());}", "statement", model), ResultFlow.NORMAL);
      assertEquals(statement("for(var i in {a:1,b:2,c:3}){out.println(i.getKey());}", "statement", model), ResultFlow.NORMAL);
      assertEquals(statement("function x(a,b,c){out.println(b);}x(1,2,3);", "script", model), ResultFlow.NORMAL);
      assertEquals(statement("return;", "return-statement", model), ResultFlow.RETURN);     
      assertEquals(statement("function one(){return 1;}var xx=one();", "script", model), ResultFlow.NORMAL);     
      
      assertEquals(executeScript("script1.js", new HashMap<String, Object>(model)), ResultFlow.NORMAL);
      assertEquals(executeScript("script9.js", new HashMap<String, Object>(model)), ResultFlow.NORMAL);
      assertEquals(executeScript("script10.js", new HashMap<String, Object>(model)), ResultFlow.NORMAL);
      assertEquals(executeScript("script11.js", new HashMap<String, Object>(model)), ResultFlow.NORMAL);
      assertEquals(executeScript("script12.js", new HashMap<String, Object>(model)), ResultFlow.NORMAL);
   }
   public static Object executeScript(String source, Map<String, Object> model) throws Exception {
      String script = load(new File("c:\\Work\\development\\github\\snapscript\\snap-parse\\src\\test\\java\\org\\snapscript\\parse\\"+source));
      return statement(script, "script", model);
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
   public static Object evaluate(String source, String grammar, Map<String, Object> model) throws Exception {
      return evaluate(source, grammar, model, 0);
   }
   public static ResultFlow statement(String source, String grammar, Map<String, Object> map) throws Exception {
      Model model = new MapModel(map);
      InstructionResolver set = new InterpretationResolver();
      Context cc =new ScriptContext(set);
      ModuleScope mod=new ModuleScope(cc.getModule());
      Scope s=new ModelScope(mod, model);
      Assembler builder = new Assembler(set, cc);
      SyntaxCompiler bb = new SyntaxCompiler();
      SyntaxParser analyzer =  bb.compile();
      SyntaxNode token = analyzer.parse(source, grammar);
      SyntaxPrinter.print(analyzer, source, grammar); // Evaluating the following
      Statement statement = (Statement)builder.assemble(token,"xx");
      return statement.execute(s).getFlow();
   }   
   public static Object evaluate(String source, String grammar, Map<String, Object> map, int repeat) throws Exception {
      Model model = new MapModel(map);
      InstructionResolver set = new InterpretationResolver();
      Context cc =new ScriptContext(set);
      ModuleScope mod=new ModuleScope(cc.getModule());
      Scope s=new ModelScope(mod, model);
      Assembler builder = new Assembler(set, cc);
      SyntaxCompiler bb = new SyntaxCompiler();
      SyntaxParser analyzer =  bb.compile();
      SyntaxNode token = analyzer.parse(source, grammar);
      SyntaxPrinter.print(analyzer, source, grammar); // Evaluating the following
      Evaluation evaluation = (Evaluation)builder.assemble(token,"xx");
      
      if(repeat > 0) {
         long start = System.currentTimeMillis();
         for(int i = 0; i < repeat; i++) {
            evaluation.evaluate(s, null);
         }
         long finish = System.currentTimeMillis();
         long duration = finish - start;
         System.err.println("Time taken for " + repeat + " iterations was "+duration);
      }
      return evaluation.evaluate(s, null).getValue();
   }
}
