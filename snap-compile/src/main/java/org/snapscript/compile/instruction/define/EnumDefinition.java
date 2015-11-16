package org.snapscript.compile.instruction.define;

import static org.snapscript.core.ResultFlow.NORMAL;

import java.util.List;

import org.snapscript.core.Function;
import org.snapscript.core.Initializer;
import org.snapscript.core.Module;
import org.snapscript.core.Result;
import org.snapscript.core.Scope;
import org.snapscript.core.Statement;
import org.snapscript.core.Type;

public class EnumDefinition extends Statement {

   private final DefaultConstructor constructor;
   private final DefaultInitializer initializer;
   private final TypeHierarchy hierarchy;
   private final EnumList list;
   private final TypeName name;
   private final TypePart[] parts;
   
   public EnumDefinition(TypeName name, TypeHierarchy hierarcy, EnumList list, TypePart... parts) {
      this.constructor = new DefaultConstructor();
      this.initializer = new DefaultInitializer();
      this.hierarchy = hierarcy;
      this.parts = parts;
      this.list = list;
      this.name = name;
   }

   /**
    * 1) Define the constructors and the variables
    * 2) instantiate the static enum keys
    * 3) init the statics
    * 
    */
   @Override
   public Result compile(Scope scope) throws Exception {
      StaticScope other = new StaticScope(scope);
      InitializerCollector duh = new InitializerCollector();
      InitializerCollector collector = new InitializerCollector();
      // this should be passed in to the ClassHierarchy to define the type hierarchy!!!
      String n=name.evaluate(other, null).getString();
      
      Module module = other.getModule();
      Type t = module.addType(n);
      List<Type>types=t.getTypes();
     
      Initializer keys = list.define(other, collector, t);
     
      types.addAll(hierarchy.create(other)); // add in the type hierarchy!!

      for(TypePart part : parts) {
         Initializer s=part.define(other, duh, t);
         collector.update(s);
      }  
      initializer.execute(scope, t);
      
      List<Function>mapL=t.getFunctions();
      int count=0;
      for(Function f:mapL){
         if(f.getName().equals("new")) {
            count++;
         }
      }
      if(count==0){
         constructor.define(other, collector, t); // add the default no arg constructor!!
      }
      keys.execute(other, t);
      collector.execute(scope, t);
      
      return NORMAL.getResult(t);
   }


}
