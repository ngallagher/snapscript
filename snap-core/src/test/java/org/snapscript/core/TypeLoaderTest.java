package org.snapscript.core;

import java.util.Arrays;

import org.snapscript.core.resource.ClassPathReader;
import org.snapscript.core.resource.ResourceReader;

import junit.framework.TestCase;

public class TypeLoaderTest extends TestCase {
   
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
   
   public void testLoader() throws Exception {
      PackageLinker linker = new PackageLinker() {
         
         @Override
         public Package link(String name, String source) throws Exception {
            return null;
         }
         @Override
         public Package link(String name, String source, String grammar) throws Exception {
            return null;
         }
      };
      long start = System.currentTimeMillis();
      
      for(int i = 0; i < 1000; i++) {
         ResourceReader reader = new ClassPathReader();
         ImportResolver resolver = new ImportResolver(linker, reader);
         TypeLoader loader = new TypeLoader(resolver);
         
         loader.loadType(ExampleObject.class);
      }
      long finish = System.currentTimeMillis();
      long duration = finish - start;
      
      System.err.println("Duration: " + duration);
   }

}
