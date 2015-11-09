package org.snapscript.core;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TypeLoader {
   
   private final Map<Object, Type> types;
   private final PrimitivePromoter converter;
   private final ImportResolver resolver;
   private final List<String> modules;
   
   public TypeLoader(ImportStore store, ImportResolver resolver){
      this.types = new LinkedHashMap<Object, Type>();      
      this.converter = new PrimitivePromoter();
      this.modules = new ArrayList<String>();
      this.resolver = resolver;
   }
   public Library addImport(String name) {
      modules.add(name);///XXX????
      return resolver.addImport(name);
   }   
   public Library addType(String location, String name) {
      modules.add(name);
      //resolver.addImport(location);// helps with java types
      
      return resolver.addType(location, name);
   }
   
   public Class getType(String name) { 
      return resolver.getType(name);
   }
   private void registerType(Object name, Type type) {
      types.put(name, type);
   }
   private Type resolveType(Object name) {
      return types.get(name);
   }
   public Type define(String name,String moduleName) throws Exception {
      String full=moduleName!=null?moduleName+"."+name:name;
      Type t  =resolveType(full);
    
      
      if(t==null) {
         StringBuilder builder=new StringBuilder();
         Map<String,Type> hier = new LinkedHashMap<String,Type>();
         List<Function> mapL = new ArrayList<Function>();
         List<Property> varsL = new ArrayList<Property>();
         List<Type> hierL = new ArrayList<Type>();

         
         t=new Type(full,null,hierL,varsL,mapL);
         registerType(full,t);
      }
      return t;
   }
   
   public Type load(String name,String moduleName) throws Exception {
      return load(name,null,true);
   }
   public Type load(String name, String moduleName, boolean create) throws Exception {
      Type t = resolveType(name);
      
      if(t==null) { 
         Class cls=resolver.getType(name);
         if(cls==null){
            for(String n:modules){// was the type named in a module???
               t=resolveType(n+"."+name);
               if(t!=null){
                  return t;
               }
            }
            if(create){
               return define(name,moduleName);
            }
            return null;
         }
         t=load(cls);
         registerType(name, t);
      }
      return t;
   }
   public Type load(final Class cls) throws Exception {
      Class type = converter.convert(cls);
      Type t=resolveType(cls);
      
      if(t==null){
         String name = type.getName();
         Class base = type.getSuperclass();
         Class[] interfaces = type.getInterfaces();
         StringBuilder builder=new StringBuilder();
         Map<String,Type> hier = new LinkedHashMap<String,Type>();
         List<Function> mapL = new ArrayList<Function>();
         List<Property> varsL = new ArrayList<Property>();
         List<Type> hierL = new ArrayList<Type>();
         Type bbse=null;
         if(base!=null) {
            bbse=load(base);
            hier.put(bbse.getName(),bbse);
         }
         Type done=null;
         if(type.isArray()){
            done=new Type(name,load(type.getComponentType()),hierL,varsL,mapL,cls);
         }else {
            done=new Type(name,null,hierL,varsL,mapL,cls);
         }
         if(type==Object.class){
            Type any=load("Any", null,true);
            hierL.add(any);
         }
         registerType(cls,done);
         if(!cls.isPrimitive()) { // need to know if a type is primitive for methods or constructors, MIGHT cause problems!!!!
            registerType(name,done);
            indexMethods(type, builder, mapL, varsL);
            indexConstructors(type, builder, mapL);
            indexFields(type, base, interfaces, hier, varsL, hierL);
         }
         hierL.addAll(hier.values());
         return done;
      }
      return t;
   }
   private void indexMethods(Class type, StringBuilder builder, List<Function> mapL, List<Property> varsL) throws Exception {
      Method[] methods = type.getDeclaredMethods();
      for(Method m:methods){
         int mod=m.getModifiers();
         if(Modifier.isPublic(mod)) {
            Class[] c=m.getParameterTypes();
            List<Type> tt=new ArrayList<Type>();
            List<String>nns=new ArrayList<String>();
            builder.setLength(0);
            builder.append(m.getName());
            builder.append("(");
            for(int i=0;i<c.length;i++){
               if(i>0){
                  builder.append(", ");
               }
               Type tp =load(c[i]);//c[i]=converter.convert(c[i]);// promote primitives
               builder.append(tp.getName());
               tt.add(tp);
               nns.add("a"+i);
            }
            builder.append(")");
            m.setAccessible(true);
            //SignatureKey k=new SignatureKey(nb,tt);
            String k=builder.toString();
            int modifiers=m.getModifiers();
            Signature sig=new Signature(nns, tt,modifiers);
            Invocation ex=new MethodInvocation(m);
            Function gg=new Function(sig, ex, k, m.getName());

            if(!Modifier.isStatic(mod)) {
               String prop=getProperty(m);
               if(prop!=null){
                  Method read=m;
                  Class readT=read.getReturnType();
                  Method write=getPropertySetterMatch(methods,readT,prop);
                  Type propT=load(readT);
                  MethodAccessor acc=new MethodAccessor(converter.convert(readT),read,write);
                  Property v=new Property(prop,propT,acc);               
                  varsL.add(v);
                  
               }
            }
            mapL.add(gg);
         }
      }
   }
   private void indexConstructors(Class type, StringBuilder builder, List<Function> mapL) throws Exception {
      Constructor[] cons = type.getDeclaredConstructors();
      for(Constructor c:cons){
         int mod=c.getModifiers();
         if(Modifier.isPublic(mod)) {
            Class[] cl=c.getParameterTypes();
            List<Type> tt=new ArrayList<Type>();
            List<String>nns=new ArrayList<String>();
            builder.setLength(0);
            builder.append("new(");
            for(int i=0;i<cl.length;i++){
               if(i>0){
                  builder.append(", ");
               }
               Type tp =load(cl[i]);//c[i]=converter.convert(c[i]);// promote primitives
               builder.append(tp.getName());
               tt.add(tp);
               nns.add("a"+i);
            }
            builder.append(")");
            c.setAccessible(true);
            //SignatureKey k=new SignatureKey("new",tt);
            String k=builder.toString();
            int modifiers=c.getModifiers();
            Signature sig=new Signature(nns, tt,modifiers);
            Invocation ex=new ConstructorInvocation(c);
            Function gg=new Function(sig, ex, k, "new");
            mapL.add(gg);
         }
      }
   }
   private void indexFields(Class type, Class base, Class[] interfaces, Map<String, Type> hier, List<Property> varsL, List<Type> hierL) throws Exception {
      Field[] fields= type.getDeclaredFields();
      for(Field f:fields){
         int mod=f.getModifiers();
         if(Modifier.isPublic(mod)) {
            String nb=f.getName();
            Type ft=load(f.getType());
            FieldAccessor acc=new FieldAccessor(f);
            Property v=new Property(nb,ft,acc);               
            varsL.add(v);
         }
      }
      for(Class i:interfaces){
         Type baset=load(i);
         hier.put(i.getName(),baset);
         hierL.addAll(baset.getTypes());
         for(Type ttp:baset.getTypes()){
            hier.put(ttp.getName(),ttp);
         }
      }
      if(base != null) {
         Type baset=load(base); 
         hier.put(base.getName(), baset);
         for(Type ttp:baset.getTypes()){
            hier.put(ttp.getName(),ttp);
         }
      }
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
   private static enum PropertyType {
      GET("get", true),
      SET("set", false),      
      IS("is", true);

      private final String prefix;
      private final boolean read;
      private final int size;

      private PropertyType(String prefix, boolean read) {
         this.size = prefix.length();         
         this.prefix = prefix;
         this.read = read;
      }
      
      public boolean isWrite(Method method) {
         String name = method.getName();
         int length = name.length();
         
         if(name.startsWith(prefix)) {
            Class type = method.getReturnType();
            Class[] types = method.getParameterTypes();
            int count = types.length;

            if(type == void.class) {
               return length > size && count == 1;
            }
         }
         return false;
      }      

      public boolean isRead(Method method) {
         String name = method.getName();
         int length = name.length();
         
         if(name.startsWith(prefix)) {
            Class type = method.getReturnType();
            Class[] types = method.getParameterTypes();
            int count = types.length;

            if(type != void.class) {
               return length > size && count == 0;
            }
         }
         return false;
      }

      public String getProperty(Method method) {
         String name = method.getName();

         if(name.startsWith(prefix)) {
            name = name.substring(size);
         }
         return getPropertyName(name);
      }
   }

   public static String getPropertyName(Method method) {
      PropertyType[] types = PropertyType.values();

      for(PropertyType matchType : types) {
         if(matchType.isRead(method)) {
            return matchType.getProperty(method);
         }
      }
      return method.getName();
   }

   public static String getPropertyName(String name) {
      int length = name.length();

      if(length > 0) {
         char[] array = name.toCharArray();
         char first = array[0];

         if(!isAcronym(array)) {
            array[0] = toLowerCase(first);
         }
         return new String(array);
      }
      return name;
   }

   private static boolean isAcronym(char[] array) {
      if(array.length < 2) {
         return false;
      }
      if(!isUpperCase(array[0])) {
         return false;
      }
      return isUpperCase(array[1]);
   }

   private static char toLowerCase(char value) {
      return Character.toLowerCase(value);
   }

   private static boolean isUpperCase(char value) {
      return Character.isUpperCase(value);
   }   
}
