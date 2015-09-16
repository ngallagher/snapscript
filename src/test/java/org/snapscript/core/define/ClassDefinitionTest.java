package org.snapscript.core.define;

import java.util.Collections;
import java.util.concurrent.Callable;

import org.snapscript.Model;
import org.snapscript.core.Context;
import org.snapscript.core.ContextModule;
import org.snapscript.core.MapModel;
import org.snapscript.core.ModelScope;
import org.snapscript.core.Module;
import org.snapscript.core.ModuleScope;
import org.snapscript.core.Scope;
import org.snapscript.core.ScriptContext;
import org.snapscript.core.Type;
import org.snapscript.core.convert.FunctionBinder;
import org.snapscript.core.define.ClassDefinition;
import org.snapscript.core.define.MemberField;
import org.snapscript.core.define.ModifierList;
import org.snapscript.core.define.TypeName;
import org.snapscript.core.define.TypePart;
import org.snapscript.core.execute.BooleanLiteral;
import org.snapscript.core.execute.NumberLiteral;
import org.snapscript.core.execute.Result;
import org.snapscript.core.execute.TextLiteral;
import org.snapscript.parse.NumberToken;
import org.snapscript.parse.StringToken;

import junit.framework.TestCase;

public class ClassDefinitionTest extends TestCase {

   public void testDefineClass() throws Exception {
      StringToken nameToken = new StringToken("Test");
      TextLiteral nameLiteral = new TextLiteral(nameToken);
      TypeName name = new TypeName(nameLiteral);
      TypePart[] parts = new TypePart[]{
            new MemberField(new ModifierList(),new TextLiteral(new StringToken("bool")), new BooleanLiteral(new StringToken("true"))),
            new MemberField(new ModifierList(),new TextLiteral(new StringToken("num")), new NumberLiteral(new NumberToken(12.33d))),            
      };
      ClassDefinition definer = new ClassDefinition(name, parts);
      Model model = new MapModel(Collections.EMPTY_MAP);
      Context context = new ScriptContext();
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
      ClassDefinition definer = new ClassDefinition(name, parts);
      Model model = new MapModel(Collections.EMPTY_MAP);
      Context context = new ScriptContext();
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