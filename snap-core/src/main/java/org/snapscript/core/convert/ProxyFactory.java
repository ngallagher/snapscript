package org.snapscript.core.convert;

import java.lang.reflect.Proxy;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

import org.snapscript.core.Any;
import org.snapscript.core.Bug;
import org.snapscript.core.Scope;
import org.snapscript.core.SuperScope;
import org.snapscript.core.Type;
import org.snapscript.core.TypeTraverser;

public class ProxyFactory {

   private final AtomicReference<ClassLoader> reference;
   private final Map<Type, Class[]> cache;
   private final TypeTraverser traverser;
   private final ProxyWrapper wrapper;
   private final Class[] empty;
   
   public ProxyFactory(ProxyWrapper wrapper) {
      this.cache = new ConcurrentHashMap<Type, Class[]>();
      this.reference = new AtomicReference<ClassLoader>();
      this.traverser = new TypeTraverser();
      this.empty = new Class[]{};
      this.wrapper = wrapper;
   }
   
   @Bug("Figure out the ClassLoader...")
   public Object create(Object value) {
      Class[] interfaces = traverse(value);
      
      if(interfaces.length > 0) {
         ClassLoader loader = reference.get();
         
         if(loader == null) {
            loader = Thread.currentThread().getContextClassLoader();
            reference.set(loader);
         }
         ProxyHandler handler = new ProxyHandler(wrapper, (Scope)value);
         return Proxy.newProxyInstance(loader, interfaces, handler);
      }
      return value;
   }
   
   private Class[] traverse(Object value) {
      Type type = extract(value);
      
      if(type != null) {
         Class[] interfaces = cache.get(type);
         
         if(interfaces == null) {
            Set<Class> types = traverse(type);
            Class[] result = types.toArray(empty);
            
            cache.put(type, result);
            return result;
         }
         return interfaces;
      }
      return empty;
   }
   
   private Set<Class> traverse(Type type) {
      Set<Type> types = traverser.traverse(type);
      
      if(!types.isEmpty()) {
         Set<Class> interfaces = new HashSet<Class>();
      
         for(Type entry : types) {
            Class part = entry.getType();
            
            if(part != null) {
               if(part.isInterface()) {
                  interfaces.add(part);
               }
            }
         }
         interfaces.add(Any.class);
         return interfaces;
      }
      return Collections.<Class>singleton(Any.class);
   }
   
   private Type extract(Object value) {
      if(SuperScope.class.isInstance(value)) {
         SuperScope scope = (SuperScope)value;
         return scope.getSuper();
      }
      if(Scope.class.isInstance(value)) {
         Scope scope = (Scope)value;
         return scope.getType();
      } 
      return null;
   }
}
