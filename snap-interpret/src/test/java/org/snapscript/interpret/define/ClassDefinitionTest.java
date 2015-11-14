package org.snapscript.interpret.define;

import java.util.Collections;
import java.util.concurrent.Callable;

import junit.framework.TestCase;

import org.snapscript.assemble.InstructionResolver;
import org.snapscript.assemble.ScriptContext;
import org.snapscript.core.Context;
import org.snapscript.core.ContextModule;
import org.snapscript.core.MapModel;
import org.snapscript.core.Model;
import org.snapscript.core.ModelScope;
import org.snapscript.core.Module;
import org.snapscript.core.ModuleScope;
import org.snapscript.core.Result;
import org.snapscript.core.Scope;
import org.snapscript.core.Type;
import org.snapscript.core.bind.FunctionBinder;
import org.snapscript.interpret.BooleanLiteral;
import org.snapscript.interpret.InterpretationResolver;
import org.snapscript.interpret.NumberLiteral;
import org.snapscript.interpret.TextLiteral;
import org.snapscript.interpret.define.ClassDefinition;
import org.snapscript.interpret.define.MemberField;
import org.snapscript.interpret.define.ModifierList;
import org.snapscript.interpret.define.TypeName;
import org.snapscript.interpret.define.TypePart;
import org.snapscript.parse.NumberToken;
import org.snapscript.parse.StringToken;

public class ClassDefinitionTest extends TestCase {

   public void testDefineClass() throws Exception {
      StringToken nameToken = new StringToken("Test");
      TextLiteral nameLiteral = new TextLiteral(nameToken);
      TypeName name = new TypeName(nameLiteral);
      TypePart[] parts = new TypePart[]{
            new MemberField(new ModifierList(),new TextLiteral(new StringToken("bool")), new BooleanLiteral(new StringToken("true"))),
            new MemberField(new ModifierList(),new TextLiteral(new StringToken("num")), new NumberLiteral(new NumberToken(12.33d))),            
      };
      TypeHierarchy hierarchy = new TypeHierarchy();
      ClassDefinition definer = new ClassDefinition(name, hierarchy, parts);
      Model model = new MapModel(Collections.EMPTY_MAP);
      InstructionResolver set = new InterpretationResolver();
      Context context =new ScriptContext(set);
      ContextModule m = new ContextModule(context);
      ModuleScope scope = new ModuleScope(m);
      ModelScope mod = new ModelScope(scope, model);
      Type type = definer.compile(mod).getValue();

      assertEquals(type.getName(), "Test");
      assertEquals(type.getProperties().size(), 3);//include 'this'            
   }

   public void testInstantiateClass() throws Exception {
      StringToken nameToken = new StringToken("Test");
      TextLiteral nameLiteral = new TextLiteral(nameToken);
      TypeName name = new TypeName(nameLiteral);
      TypePart[] parts = new TypePart[]{
            new MemberField(new ModifierList(),new TextLiteral(new StringToken("bool")), new BooleanLiteral(new StringToken("true"))),
            new MemberField(new ModifierList(),new TextLiteral(new StringToken("num")), new NumberLiteral(new NumberToken(12.33d))),            
      };
      TypeHierarchy hierarchy = new TypeHierarchy();
      ClassDefinition definer = new ClassDefinition(name, hierarchy, parts);
      Model model = new MapModel(Collections.EMPTY_MAP);
      InstructionResolver set = new InterpretationResolver();
      Context context =new ScriptContext(set);
      ContextModule m = new ContextModule(context);
      Module module = new ContextModule(context);
      ModuleScope scope = new ModuleScope(module);
      ModelScope mod = new ModelScope(scope, model);

      Type type = definer.compile(mod).getValue(); 
      FunctionBinder binder = context.getBinder();
      Callable<Result> call = binder.bind(scope, type, "new");
      Scope result = call.call().getValue();
            
      assertNotNull(result);
      assertNotNull(result.getValue("bool"));
      assertNotNull(result.getValue("num"));
      assertEquals(result.getValue("bool").getValue(),Boolean.TRUE);
      assertEquals(result.getValue("num").getValue(),12.33d);
   }
}
