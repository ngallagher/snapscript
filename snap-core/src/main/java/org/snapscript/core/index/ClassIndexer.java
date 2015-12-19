package org.snapscript.core.index;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.snapscript.core.Bug;
import org.snapscript.core.Function;
import org.snapscript.core.PrimitivePromoter;
import org.snapscript.core.Property;
import org.snapscript.core.Type;

@Bug("This is rubbish and needs to be cleaned up, also consider TypeReference cache so we can partially index")
public class ClassIndexer {

   private final ConstructorIndexer constructors;
   private final FunctionIndexer functions;
   private final PropertyIndexer properties;
   private final PrimitivePromoter promoter;
   private final TypeIndexer indexer;
   private final TypeCache cache;
   
   public ClassIndexer(TypeIndexer indexer, TypeCache cache){
      this.constructors = new ConstructorIndexer(indexer);
      this.properties = new PropertyIndexer(indexer);
      this.functions = new FunctionIndexer(indexer);
      this.promoter = new PrimitivePromoter();
      this.indexer = indexer;
      this.cache = cache;
   }
   
   public synchronized Type index(Class cls) throws Exception {
      Class type = promoter.promote(cls);
      Type t=cache.resolveType(cls); // XXX there should be some form of TypeCache
      
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

         Type bbse=null;
         if(base!=null) {
            bbse=indexer.load(base);
            hier.put(bbse.getName(),bbse);
         }
         Type done=null;
         if(type.isArray()){
            done=new ClassType(simpleName,pack,indexer.load(type.getComponentType()),cls);
         }else {
            done=new ClassType(simpleName,pack,null,cls);
         }
         List<Function> functions = done.getFunctions();
         List<Property> properties = done.getProperties();
         List<Type> types = done.getTypes();
         
         if(type==Object.class){
            Type any=indexer.load("Any", null,true);// XXX there should be some form of TypeCache
            hier.put("Any",any);
         }
         cache.registerType(cls,done);
         for(Class i:interfaces){
            Type baset=indexer.load(i);
            hier.put(i.getName(),baset);
            //hierL.addAll(baset.getTypes());
            //for(Type ttp:baset.getTypes()){
            //   hier.put(ttp.getName(),ttp);
            //}
         }
         if(!cls.isPrimitive()&&!cls.isArray()) { // need to know if a type is primitive for methods or constructors, MIGHT cause problems!!!!
            cache.registerType(key,done);// XXX there should be some form of TypeCache
            List<Function> f = this.functions.index(type);
            List<Property> pd = this.properties.index(type);
            List<Function> cons = this.constructors.index(type);
            functions.addAll(f);
            properties.addAll(pd);
            functions.addAll(cons);
         }
         types.addAll(hier.values());
         return done;
      }
      return t;
   }
}
