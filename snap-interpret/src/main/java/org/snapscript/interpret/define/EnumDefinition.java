package org.snapscript.interpret.define;

import static org.snapscript.core.ResultFlow.NORMAL;

import java.util.List;

import org.snapscript.core.Function;
import org.snapscript.core.Initializer;
import org.snapscript.core.Module;
import org.snapscript.core.Property;
import org.snapscript.core.Result;
import org.snapscript.core.Scope;
import org.snapscript.core.ScopeAccessor;
import org.snapscript.core.Statement;
import org.snapscript.core.Type;

public class EnumDefinition extends Statement {

   private final AnyDefinition definition;
   private final EnumList list;
   private final TypeName name;
   private final TypePart[] parts;
   
   public EnumDefinition(TypeName name, EnumList list, TypePart... parts) {
      this.definition = new AnyDefinition();
      this.parts = parts;
      this.list = list;
      this.name = name;
   }

   /**
    * 1) Define the constructors and the variables
    * 2) instantiate the static enum keys
    * 
    * 
    */
   @Override
   public Result compile(Scope scope) throws Exception {
      StaticScope other = new StaticScope(scope);
      InitializerCollector collector = new InitializerCollector();
      
      // this should be passed in to the ClassHierarchy to define the type hierarchy!!!
      String n=name.evaluate(other, null).getString();
      
      Module module = other.getModule();
      Type t = module.addType(n);
      Result result = definition.execute(scope);
      Type superT = result.getValue();
      List<Type> types = t.getTypes();
      
      types.add(superT);
     
      for(TypePart part : parts) {
         Initializer init=part.define(other, collector, t);
         collector.update(init);
      }
      ScopeAccessor accessor = new ScopeAccessor("this");
      ScopeAccessor accessor2 = new ScopeAccessor("class");
      Property property = new Property("this", t, accessor);
      Property property2 = new Property("class", t, accessor2);      
      t.getProperties().add(property);
      List<Function>mapL=t.getFunctions();
      int count=0;
      for(Function f:mapL){
         if(f.getName().equals("new")) {
            count++;
         }
      }
      if(count==0){
         new DefaultConstructor().define(other, collector, t); // add the default no arg constructor!!
      }
      list.define(scope, collector, t); // create all of the static values
      collector.initialize(scope, t); // from there
      
      return NORMAL.getResult(t);
   }


}
