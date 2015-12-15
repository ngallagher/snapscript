package org.snapscript.core.index;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.snapscript.core.Bug;
import org.snapscript.core.FieldAccessor;
import org.snapscript.core.Function;
import org.snapscript.core.Invocation;
import org.snapscript.core.MethodAccessor;
import org.snapscript.core.MethodInvocation;
import org.snapscript.core.PrimitivePromoter;
import org.snapscript.core.Property;
import org.snapscript.core.PropertyType;
import org.snapscript.core.Signature;
import org.snapscript.core.Type;

@Bug("This is rubbish and needs to be cleaned up")
public class PropertyIndexer {
   
   private final PrimitivePromoter promoter;
   private final TypeIndexer indexer;
   
   public PropertyIndexer(TypeIndexer indexer){
      this.promoter = new PrimitivePromoter();
      this.indexer = indexer;
   }

   public List<Property> index(Class type) throws Exception {
      List<Property> properties = new ArrayList<Property>();
      Field[] fields= type.getDeclaredFields();
      for(Field f:fields){
         int mod=f.getModifiers();
         if(Modifier.isPublic(mod)) {
            String nb=f.getName();
            Type ft=indexer.load(f.getType());
            FieldAccessor acc=new FieldAccessor(f);
            Property v=new Property(nb,ft,acc);               
            properties.add(v);
         }
      }
//      for(Class i:interfaces){
//         Type baset=indexer.load(i);
//         hier.put(i.getName(),baset);
//         //hierL.addAll(baset.getTypes());
//         //for(Type ttp:baset.getTypes()){
//         //   hier.put(ttp.getName(),ttp);
//         //}
//      }
//      //if(base != null) {
//         //Type baset=load(base); 
//         //hier.put(base.getName(), baset);
//         //for(Type ttp:baset.getTypes()){
//         //   hier.put(ttp.getName(),ttp);
//         //}
//      //}
      Method[] methods = type.getDeclaredMethods();
      for(Method m:methods){
         int mod=m.getModifiers();
         if(Modifier.isPublic(mod)) {
            Class[] c=m.getParameterTypes();
            if(c.length == 0) {
   
               m.setAccessible(true);
               //SignatureKey k=new SignatureKey(nb,tt);
               int modifiers=m.getModifiers();
               Signature sig=new Signature(Collections.EMPTY_LIST, Collections.EMPTY_LIST, modifiers);
               Invocation ex=new MethodInvocation(m);
               Function gg=new Function(sig, ex, m.getName());
   
               if(!Modifier.isStatic(mod)) {
                  String prop=getProperty(m);
                  if(prop!=null){
                     Method read=m;
                     Class readT=read.getReturnType();
                     Method write=getPropertySetterMatch(methods,readT,prop);
                     if(write!=null){
                        write.setAccessible(true);
                     }
                     Type propT=indexer.load(readT);
                     MethodAccessor acc=new MethodAccessor(promoter.convert(readT),read,write);
                     Property v=new Property(prop,propT,acc);               
                     properties.add(v);
                     
                  }
               }
            }
         }
      }
      return properties;
   }
   
   private String getProperty(Method method)throws Exception{
      PropertyType[] types = PropertyType.values();
      
      for(PropertyType matchType : types) {
         if(matchType.isRead(method)) {
            return matchType.getProperty(method);
         }
      }
      return null;
   }
   
   private Method getPropertySetterMatch(Method[] methods, Class require, String name) throws Exception {
      PropertyType[] types = PropertyType.values();

      for(Method method : methods) {         
         int modifiers = method.getModifiers();
         
         if(!Modifier.isStatic(modifiers) && Modifier.isPublic(modifiers)) {
            for(PropertyType matchType : types) {            
               if(matchType.isWrite(method)) {
                  Class[] param = method.getParameterTypes();
                  Class actual = param[0];
                  
                  if(actual == require) {
                     String property = matchType.getProperty(method);
      
                     if(property.equalsIgnoreCase(name)) {
                        return method;
                     }
                  }
               }
            }
         }
      }
      return null;
   }
}
