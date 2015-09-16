package org.snapscript.core.convert;

import java.util.Arrays;

import org.snapscript.core.Type;
import org.snapscript.core.TypeLoader;
import org.snapscript.core.TypeResolver;
import org.snapscript.core.convert.FunctionMatcher;
import org.snapscript.core.convert.FunctionPointer;
import org.snapscript.core.execute.Library;
import org.snapscript.core.execute.LibraryLinker;

import junit.framework.TestCase;

public class FunctionMatcherTest extends TestCase {
   
   public static class ExampleObject {
      public void method(String s, int i, double d){
         System.out.println("method(String s="+s+", int i="+i+", double d="+d+")");
      }
      public void method(String s, Number n, double d){
         System.out.println("method(String s="+s+", Number n="+n+", double d="+d+")");
      }
      public void method(String s, long l, float d){
         System.out.println("method(String s="+s+", long l="+l+", float d="+d+")");
      }
      public void method(String s, String l, Integer... i){
         System.out.println("method(String s="+s+", String l="+l+", Integer... i)="+Arrays.asList(i)+"");
      }
   }
   
   public void testMatcher() throws Exception {
      ExampleObject object = new ExampleObject();
      LibraryLinker linker = new LibraryLinker() {
         
         @Override
         public Library link(String source) throws Exception {
            return null;
         }
      };
      TypeResolver resolver = new TypeResolver(linker);
      TypeLoader loader = new TypeLoader(resolver);
      FunctionMatcher matcher = new FunctionMatcher(loader);
      Type type = loader.load(ExampleObject.class);
      
      assertNotNull(type);
      
      FunctionPointer call1 = matcher.match(type, "method", "x", 12, 44f);
      FunctionPointer call2 = matcher.match(type, "method", "x", 12d, 44f);
      FunctionPointer call3 = matcher.match(type, "method", "x", 1L, 44f);
      FunctionPointer call4 = matcher.match(type, "method", "x", "l", 2,3,4,5);
      FunctionPointer call5 = matcher.match(type, "method", "x", 10, 10);
      FunctionPointer call6 = matcher.match(type, "method", "x", "10", "10");
      
      call1.call(null, object);
      call2.call(null, object);
      call3.call(null, object);
      call4.call(null, object);
      call5.call(null, object);
      call6.call(null, object);
      
      call1 = matcher.match(type, "method", "x", 12, 44f);
      call2 = matcher.match(type, "method", "x", 12d, 44f);
      call3 = matcher.match(type, "method", "x", 1L, 44f);
      call4 = matcher.match(type, "method", "x", "l", 2,3,4,5);
      call5 = matcher.match(type, "method", "x", 10, 10);
      call6 = matcher.match(type, "method", "x", "10", "10");
      
      call1.call(null, object);
      call2.call(null, object);
      call3.call(null, object);
      call4.call(null, object);
      call5.call(null, object);
      call6.call(null, object);

   }

}
