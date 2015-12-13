package org.snapscript.core.index;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.snapscript.core.ConstructorInvocation;
import org.snapscript.core.Function;
import org.snapscript.core.Invocation;
import org.snapscript.core.Signature;
import org.snapscript.core.Type;

public class ConstructorIndexer {

   private final TypeIndexer indexer;
   
   public ConstructorIndexer(TypeIndexer indexer) {
      this.indexer = indexer;
   }

   public List<Function> index(Class type) throws Exception {
      List<Function> functions = new ArrayList<Function>();
      Constructor[] cons = type.getDeclaredConstructors();
      for(Constructor c:cons){
         int mod=c.getModifiers();
         if(Modifier.isPublic(mod)) {
            Class[] cl=c.getParameterTypes();
            List<Type> tt=new ArrayList<Type>();
            List<String>nns=new ArrayList<String>();
            for(int i=0;i<cl.length;i++){
               Type tp =indexer.load(cl[i]);//c[i]=converter.convert(c[i]);// promote primitives
               tt.add(tp);
               nns.add("a"+i);
            }
            c.setAccessible(true);
            int modifiers=c.getModifiers();
            Signature sig=new Signature(nns, tt,modifiers);
            Invocation ex=new ConstructorInvocation(c);
            Function gg=new Function(sig, ex, "new");
            functions.add(gg);
         }
      }
      return functions;
   }
}
