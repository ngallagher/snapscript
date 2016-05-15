package org.snapscript.compile.instruction.define;

import static org.snapscript.core.Reserved.DEFAULT_PACKAGE;

import java.util.concurrent.Callable;

import junit.framework.TestCase;

import org.snapscript.compile.StoreContext;
import org.snapscript.compile.instruction.AnnotationList;
import org.snapscript.compile.instruction.literal.BooleanLiteral;
import org.snapscript.compile.instruction.literal.NumberLiteral;
import org.snapscript.compile.instruction.literal.TextLiteral;
import org.snapscript.core.Context;
import org.snapscript.core.ContextModule;
import org.snapscript.core.Module;
import org.snapscript.core.ModuleScope;
import org.snapscript.core.Property;
import org.snapscript.core.Result;
import org.snapscript.core.Scope;
import org.snapscript.core.Type;
import org.snapscript.core.bind.FunctionBinder;
import org.snapscript.core.store.ClassPathStore;
import org.snapscript.core.store.Store;
import org.snapscript.parse.NumberToken;
import org.snapscript.parse.StringToken;

public class ClassDefinitionTest extends TestCase {

   public void testDefineClass() throws Exception {
      StringToken nameToken = new StringToken("Test");
      TextLiteral nameLiteral = new TextLiteral(nameToken);
      TypeName name = new TypeName(nameLiteral);
      TypePart[] parts = new TypePart[]{
            new MemberField(new MemberDeclaration(),new TextLiteral(new StringToken("bool")), new BooleanLiteral(new StringToken("true"))),
            new MemberField(new MemberDeclaration(),new TextLiteral(new StringToken("num")), new NumberLiteral(new NumberToken(12.33d))),            
      };
      TypeHierarchy hierarchy = new TypeHierarchy();
      ClassDefinition definer = new ClassDefinition(new AnnotationList(), name, hierarchy, parts);
      Store store = new ClassPathStore();
      Context context =new StoreContext(store);
      ContextModule module = new ContextModule(context, DEFAULT_PACKAGE, DEFAULT_PACKAGE);
      ModuleScope scope = new ModuleScope(module);
      Type type = definer.compile(scope).getValue();

      for(Property property : type.getProperties()) {
         System.err.println(property.getName());
      }
      assertEquals(type.getName(), "Test");
      assertEquals(type.getProperties().size(), 5);//include 'this' and 'class' 'super'
      
      System.err.println(type.getProperties());
   }

   public void testInstantiateClass() throws Exception {
      StringToken nameToken = new StringToken("Test");
      TextLiteral nameLiteral = new TextLiteral(nameToken);
      TypeName name = new TypeName(nameLiteral);
      TypePart[] parts = new TypePart[]{
            new MemberField(new MemberDeclaration(),new TextLiteral(new StringToken("bool")), new BooleanLiteral(new StringToken("true"))),
            new MemberField(new MemberDeclaration(),new TextLiteral(new StringToken("num")), new NumberLiteral(new NumberToken(12.33d))),            
      };
      TypeHierarchy hierarchy = new TypeHierarchy();
      ClassDefinition definer = new ClassDefinition(new AnnotationList(), name, hierarchy, parts);
      Store store = new ClassPathStore();
      Context context =new StoreContext(store);
      Module module = new ContextModule(context, DEFAULT_PACKAGE, DEFAULT_PACKAGE);
      ModuleScope scope = new ModuleScope(module);

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
