package org.snapscript.core.index;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.snapscript.core.Function;
import org.snapscript.core.Invocation;
import org.snapscript.core.MethodInvocation;
import org.snapscript.core.Signature;
import org.snapscript.core.Type;

public class FunctionIndexer {
   
   private final TypeIndexer indexer;
   
   public FunctionIndexer(TypeIndexer indexer){
      this.indexer = indexer;
   }

   public List<Function> index(Class type) throws Exception {
      List<Function> functions = new ArrayList<Function>();
      Method[] methods = type.getDeclaredMethods();
      for(Method m:methods){
         int mod=m.getModifiers();
         if(Modifier.isPublic(mod)) {
            Class[] c=m.getParameterTypes();
            List<Type> tt=new ArrayList<Type>();
            List<String>nns=new ArrayList<String>();

            for(int i=0;i<c.length;i++){
               Type tp =indexer.load(c[i]);//c[i]=converter.convert(c[i]);// promote primitives

               tt.add(tp);
               nns.add("a"+i);
            }
            m.setAccessible(true);
            //SignatureKey k=new SignatureKey(nb,tt);
            int modifiers=m.getModifiers();
            Signature sig=new Signature(nns, tt,modifiers);
            Invocation ex=new MethodInvocation(m);
            Function gg=new Function(sig, ex, m.getName());

            functions.add(gg);
         }
      }
      return functions;
   }
}
