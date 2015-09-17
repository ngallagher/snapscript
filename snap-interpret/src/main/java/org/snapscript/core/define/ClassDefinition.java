package org.snapscript.core.define;

import static org.snapscript.core.ResultFlow.NORMAL;

import java.util.List;

import org.snapscript.core.Function;
import org.snapscript.core.Module;
import org.snapscript.core.Property;
import org.snapscript.core.Result;
import org.snapscript.core.Scope;
import org.snapscript.core.ScopeAccessor;
import org.snapscript.core.Statement;
import org.snapscript.core.Type;

public class ClassDefinition extends Statement {   
   
   private final TypeHierarchy hierarchy;
   private final TypeName name;
   private final TypePart[] parts;
   
   public ClassDefinition(TypeName name, TypePart... parts) {
      this(name, null, parts);   
   }
   
   public ClassDefinition(TypeName name, TypeHierarchy hierarchy, TypePart... parts) {
      this.hierarchy = hierarchy;
      this.parts = parts;
      this.name = name;
   }

   @Override
   public Result compile(Scope scope) throws Exception {
      StatementCollector collector = new StatementCollector();
      
      // this should be passed in to the ClassHierarchy to define the type hierarchy!!!
      String n=name.evaluate(scope, null).getString();
      Module module = scope.getModule();
      Type t = module.addType(n);
      List<Type>types=t.getTypes();
      if(hierarchy!=null){
         types.addAll(hierarchy.create(scope)); // add in the type hierarchy!!
      } 
      for(TypePart part : parts) {
         Statement s=part.define(scope, collector, t);
         collector.update(s);
      }  
      ScopeAccessor accessor = new ScopeAccessor("this");
      ScopeAccessor accessor2 = new ScopeAccessor("class");
      Property property = new Property("this", t, accessor);
      Property property2 = new Property("class", t, accessor);      
      t.getProperties().add(property);
      List<Function>mapL=t.getFunctions();
      int count=0;
      for(Function f:mapL){
         if(f.getName().equals("new")) {
            count++;
         }
      }
      if(count==0){
         new DefaultConstructor().define(scope, collector, t); // add the default no arg constructor!!
      }
      return NORMAL.getResult(t);
   }

}
