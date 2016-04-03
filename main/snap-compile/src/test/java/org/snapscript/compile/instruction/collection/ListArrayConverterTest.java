package org.snapscript.compile.instruction.collection;

import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;

import org.snapscript.compile.StoreContext;
import org.snapscript.core.Context;
import org.snapscript.core.Type;
import org.snapscript.core.TypeLoader;
import org.snapscript.core.convert.ConstraintConverter;
import org.snapscript.core.convert.ConstraintMatcher;
import org.snapscript.core.convert.Score;
import org.snapscript.core.store.ClassPathStore;
import org.snapscript.core.store.Store;

public class ListArrayConverterTest extends TestCase {

   public void testListConvertToArray() throws Exception {
      Store store = new ClassPathStore();
      Context context = new StoreContext(store);
      List compatible1 = Arrays.asList(1, 2.2d, 11L, 22L);
      List compatible2 = Arrays.asList("1", 2.2d, 11L, "22");
      List incompatible1 = Arrays.asList("1", 2.2d, 11L, Arrays.asList("22"));
      List incompatible2 = Arrays.asList("1", 2.2d, 11L, "foo");
      
      assertTrue(accept(context, byte[].class, compatible1));
      assertTrue(accept(context, byte[].class, compatible2));
      assertFalse(accept(context, byte[][].class, compatible2));
      assertFalse(accept(context, byte[].class, incompatible1));
      assertFalse(accept(context, byte[].class, incompatible2));
      assertTrue(accept(context, byte[][].class, Arrays.asList(compatible1, compatible2)));
      assertFalse(accept(context, byte[].class, Arrays.asList(compatible1, compatible2)));
      
      assertTrue(accept(context, int[].class, compatible1));
      assertTrue(accept(context, int[].class, compatible2));
      assertFalse(accept(context, int[][].class, compatible2));
      assertFalse(accept(context, int[].class, incompatible1));
      assertFalse(accept(context, int[].class, incompatible2));
      assertTrue(accept(context, int[][].class, Arrays.asList(compatible1, compatible2)));
      assertFalse(accept(context, int[].class, Arrays.asList(compatible1, compatible2)));
      
      assertTrue(accept(context, Double[].class, compatible1));
      assertTrue(accept(context, Double[].class, compatible2));
      assertFalse(accept(context, Double[][].class, compatible2));
      assertFalse(accept(context, Double[].class, incompatible1));
      assertFalse(accept(context, Double[].class, incompatible2));
      assertTrue(accept(context, Double[][].class, Arrays.asList(compatible1, compatible2)));
      assertFalse(accept(context, Double[].class, Arrays.asList(compatible1, compatible2)));
      
      assertTrue(accept(context, String[].class, compatible1));
      assertTrue(accept(context, String[].class, compatible2));
      assertFalse(accept(context, String[][].class, compatible2));
      assertTrue(accept(context, String[].class, incompatible1));
      assertTrue(accept(context, String[].class, incompatible2));
      assertTrue(accept(context, String[][].class, Arrays.asList(compatible1, compatible2)));
      assertTrue(accept(context, String[].class, Arrays.asList(compatible1, compatible2)));
      
      System.err.println(Arrays.toString((int[])convert(context, int[].class, compatible1)));
      System.err.println(Arrays.toString((int[])convert(context, int[].class, compatible2)));
      System.err.println(Arrays.deepToString((int[][])convert(context, int[][].class, Arrays.asList(compatible1, compatible2))));
   }
   
   public void testArrayConvertToArray() throws Exception {
      Store store = new ClassPathStore();
      Context context = new StoreContext(store);
      float[] compatible1 = new float[]{1f, 2.2f, 11f, 22f};
      double[] compatible2 = new double[]{1d, 0d, 11d, 2.33322d};
      
      assertTrue(accept(context, byte[].class, compatible1));
      assertTrue(accept(context, byte[].class, compatible2));
      assertFalse(accept(context, byte[][].class, compatible2));
      assertTrue(accept(context, byte[][].class, new float[][]{compatible1, compatible1}));
      assertFalse(accept(context, byte[].class, new float[][]{compatible1, compatible1}));
      
      assertTrue(accept(context, int[].class, compatible1));
      assertTrue(accept(context, int[].class, compatible2));
      assertFalse(accept(context, int[][].class, compatible2));
      assertTrue(accept(context, int[][].class, new float[][]{compatible1, compatible1}));
      assertFalse(accept(context, int[].class, new float[][]{compatible1, compatible1}));
      
      assertTrue(accept(context, Double[].class, compatible1));
      assertTrue(accept(context, Double[].class, compatible2));
      assertFalse(accept(context, Double[][].class, compatible2));
      assertTrue(accept(context, Double[][].class, new float[][]{compatible1, compatible1}));
      assertFalse(accept(context, Double[].class, new float[][]{compatible1, compatible1}));
      
      assertTrue(accept(context, String[].class, compatible1));
      assertTrue(accept(context, String[].class, compatible2));
      assertFalse(accept(context, String[][].class, compatible2));
      assertTrue(accept(context, String[][].class, new float[][]{compatible1, compatible1}));
      assertTrue(accept(context, String[].class, new float[][]{compatible1, compatible1}));
      
      System.err.println(Arrays.toString((int[])convert(context, int[].class, compatible1)));
      System.err.println(Arrays.toString((int[])convert(context, int[].class, compatible2)));
      System.err.println(Arrays.deepToString((int[][])convert(context, int[][].class, Arrays.asList(compatible1, compatible2))));
   }
   
   private Object convert(Context context, Class type, Object object) throws Exception {
      TypeLoader loader = context.getLoader();
      Type match = loader.loadType(type);
      ConstraintMatcher matcher = context.getMatcher();
      ConstraintConverter converter = matcher.match(match);
      
      return converter.convert(object);
   }
   
   private boolean accept(Context context, Class type, Object object) throws Exception {
      TypeLoader loader = context.getLoader();
      Type match = loader.loadType(type);
      ConstraintMatcher matcher = context.getMatcher();
      ConstraintConverter converter = matcher.match(match);
      
      return converter.score(object).compareTo(Score.INVALID) > 0;
   }
}
