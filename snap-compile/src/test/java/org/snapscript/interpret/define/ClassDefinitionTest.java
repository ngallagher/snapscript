package org.snapscript.interpret.define;

import java.util.Collections;
import java.util.concurrent.Callable;

import junit.framework.TestCase;

import org.snapscript.compile.ClassPathContext;
import org.snapscript.compile.instruction.BooleanLiteral;
import org.snapscript.compile.instruction.NumberLiteral;
import org.snapscript.compile.instruction.TextLiteral;
import org.snapscript.compile.instruction.define.ClassDefinition;
import org.snapscript.compile.instruction.define.MemberField;
import org.snapscript.compile.instruction.define.ModifierList;
import org.snapscript.compile.instruction.define.TypeHierarchy;
import org.snapscript.compile.instruction.define.TypeName;
import org.snapscript.compile.instruction.define.TypePart;
import org.snapscript.core.Context;
import org.snapscript.core.ContextModule;
import org.snapscript.core.MapModel;
import org.snapscript.core.Model;
import org.snapscript.core.Module;
import org.snapscript.core.ModuleScope;
import org.snapscript.core.Result;
import org.snapscript.core.Scope;
import org.snapscript.core.Type;
import org.snapscript.core.bind.FunctionBinder;
import org.snapscript.core.resource.ClassPathReader;
import org.snapscript.core.resource.ResourceReader;
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
      Context context =new ClassPathContext(model);
      ContextModule m = new ContextModule(context, model);
      ModuleScope scope = new ModuleScope(m, model);
      Type type = definer.compile(scope).getValue();

      assertEquals(type.getName(), "Test");
      assertEquals(type.getProperties().size(), 4);//include 'this' and 'class'  
      
      System.err.println(type.getProperties());
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
      Context context =new ClassPathContext(model);
      ContextModule m = new ContextModule(context, model);
      Module module = new ContextModule(context, model);
      ModuleScope scope = new ModuleScope(module, model);

      Type type = definer.compile(scope).getValue(); 
      FunctionBinder binder = context.getBinder();
      Callable<Result> call = binder.bind(scope, type, "new", type);
      Scope result = call.call().getValue();
            
      assertNotNull(result);
      assertNotNull(result.getState().getValue("bool"));
      assertNotNull(result.getState().getValue("num"));
      assertEquals(result.getState().getValue("bool").getValue(),Boolean.TRUE);
      assertEquals(result.getState().getValue("num").getValue(),12.33d);
   }
}
