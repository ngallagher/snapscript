package org.snapscript.core;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TypeIndexer {

   private final Map<Object, Type> types;
   private final PrimitivePromoter converter;
   private final ImportResolver resolver;
   private final List<String> modules;
   
   public TypeIndexer(ImportResolver resolver){
      this.types = new LinkedHashMap<Object, Type>();      
      this.converter = new PrimitivePromoter();
      this.modules = new ArrayList<String>();
      this.resolver = resolver;
   }
   public Package addImport(String name) {
      modules.add(name);///XXX????
      return resolver.addImport(name);
   }   
   public Package addType(final String name, final String module) {
      
      //modules.add(module);
      //resolver.addImport(location);// helps with java types
      
      Package library = resolver.addType(name, module);
      if(library == null) {
         return new Package() {

            @Override
            public void include(Scope scope) throws Exception {
               String full = createName(name, module);
               System.err.println("TypeIndexer.addType() Doing a CLASS load on name=["+name+"] module=["+module+"] full=["+full+"]");
               load(name, module);
            }
            
         };
      }
      return library;
   }
   
   private Class getType(String name) { 
      return resolver.getType(name);
   }
   private void registerType(Object name, Type type) {
      types.put(name, type);
   }
   private Type resolveType(Object name) {
      return types.get(name);
   }
   private Type define(String name,String moduleName) throws Exception {
      String full=createName(name, moduleName);
      Type t  =resolveType(full);
    
      
      if(t==null) {
         List<Function> mapL = new ArrayList<Function>();
         List<Property> varsL = new ArrayList<Property>();
         List<Type> hierL = new ArrayList<Type>();

         
         t=new Type(name, moduleName,null,hierL,varsL,mapL);
         registerType(full,t);
      }
      return t;
   }
   private String createName(String name, String module){
      if(module != null && module.length() >0) {
         return module + "." + name;
      }
      return name;
   }
   
   public Type load(String name,String moduleName) throws Exception {
      return load(name,moduleName,true); // XXX moduleName was null here?????
   }
   public Type load(String nameX, String moduleName, boolean create) throws Exception {
      String name = createName(nameX, moduleName);
      Type t = resolveType(name);
      
      if(t==null) { 
         System.err.println("TypeIndexer.load() Trying to load [" + name + "]");
         Class cls=resolver.getType(name);
         if(cls==null){
            for(String n:modules){// was the type named in a module???
               t=resolveType(n+"."+name);
               if(t!=null){
                  return t;
               }
            }
            if(create){
               return define(nameX,moduleName);
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
         String key = type.getName();
         java.lang.Package p = type.getPackage();
         String pack="";
         if(p!=null){
            pack=p.getName();
         }else {
            pack="";// debug types
         }
         String simpleName = type.getSimpleName();
         Class base = type.getSuperclass();
         Class[] interfaces = type.getInterfaces();
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
            done=new Type(simpleName,pack,load(type.getComponentType()),hierL,varsL,mapL,cls);
         }else {
            done=new Type(simpleName,pack,null,hierL,varsL,mapL,cls);
         }
         if(type==Object.class){
            Type any=load("Any", null,true);
            hierL.add(any);
         }
         registerType(cls,done);
         if(!cls.isPrimitive()&&!cls.isArray()) { // need to know if a type is primitive for methods or constructors, MIGHT cause problems!!!!
            registerType(key,done);
            indexMethods(type, mapL, varsL);
            indexConstructors(type, mapL);
            indexFields(type, base, interfaces, hier, varsL, hierL);
         }
         hierL.addAll(hier.values());
         return done;
      }
      return t;
   }
   private void indexMethods(Class type, List<Function> mapL, List<Property> varsL) throws Exception {
      Method[] methods = type.getDeclaredMethods();
      for(Method m:methods){
         int mod=m.getModifiers();
         if(Modifier.isPublic(mod)) {
            Class[] c=m.getParameterTypes();
            List<Type> tt=new ArrayList<Type>();
            List<String>nns=new ArrayList<String>();

            for(int i=0;i<c.length;i++){
               Type tp =load(c[i]);//c[i]=converter.convert(c[i]);// promote primitives

               tt.add(tp);
               nns.add("a"+i);
            }
            m.setAccessible(true);
            //SignatureKey k=new SignatureKey(nb,tt);
            int modifiers=m.getModifiers();
            Signature sig=new Signature(nns, tt,modifiers);
            Invocation ex=new MethodInvocation(m);
            Function gg=new Function(sig, ex, m.getName());

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
   private void indexConstructors(Class type, List<Function> mapL) throws Exception {
      Constructor[] cons = type.getDeclaredConstructors();
      for(Constructor c:cons){
         int mod=c.getModifiers();
         if(Modifier.isPublic(mod)) {
            Class[] cl=c.getParameterTypes();
            List<Type> tt=new ArrayList<Type>();
            List<String>nns=new ArrayList<String>();
            for(int i=0;i<cl.length;i++){
               Type tp =load(cl[i]);//c[i]=converter.convert(c[i]);// promote primitives
               tt.add(tp);
               nns.add("a"+i);
            }
            c.setAccessible(true);
            int modifiers=c.getModifiers();
            Signature sig=new Signature(nns, tt,modifiers);
            Invocation ex=new ConstructorInvocation(c);
            Function gg=new Function(sig, ex, "new");
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
  
}
