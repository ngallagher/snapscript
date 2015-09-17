package org.snapscript.core.define;

import org.snapscript.core.Constant;
import org.snapscript.core.Holder;
import org.snapscript.core.Property;
import org.snapscript.core.Result;
import org.snapscript.core.ResultFlow;
import org.snapscript.core.Scope;
import org.snapscript.core.Statement;
import org.snapscript.core.StaticAccessor;
import org.snapscript.core.Type;
import org.snapscript.core.Value;
import org.snapscript.core.execute.ArgumentList;
import org.snapscript.core.execute.Evaluation;
import org.snapscript.core.execute.FunctionInvocation;

public class EnumValue {
   
   private final TypeName identifier;
   private final ArgumentList list;
   
   public EnumValue(TypeName identifier) {
      this(identifier, null);
   }
   
   public EnumValue(TypeName identifier, ArgumentList list) {    
      this.identifier = identifier;
      this.list = list;
   }

   public void define(Scope scope, Type type, int index) throws Exception { // declare variable
      //DeclarationStatement s = new DeclarationStatement(identifier, constraint, value);
      Value value = identifier.evaluate(scope, null);
      Name consName = new Name();
      Evaluation evaluation= new FunctionInvocation(consName, list);     
      String name = value.getString();
      String qualifier = type.getName();
      Statement fieldDef = new FieldDefinition(evaluation,type,name,name,index);
      StaticAccessor accessor = new StaticAccessor(fieldDef,scope, name);
      
      Property property = new Property(name, type, accessor);
      
      // XXX add properties!!!
      type.getProperties().add(property);
      // XXX property needs to go in to the definition of the type...
      //statement.execute(scope);
   }


   public final class Name implements Evaluation {
   
      @Override
      public Value evaluate(Scope scope, Object left) throws Exception {
         return new Holder("new");
      }
      
   }
   public class FieldDefinition extends Statement{
      private final Evaluation ev;
      private final String name;
      private final String title;
      private final Type tp;
      private final int index;
      public FieldDefinition(Evaluation ev,Type tp,String name,String title,int index){
         this.title = title;
         this.name=name;
         this.index =index;
         this.tp=tp;
         this.ev=ev;
      }
      

      @Override
      public Result execute(Scope scope) throws Exception {
         TypeScope s = new TypeScope(scope, tp);
         Constant cst=new Constant(tp);
         s.addConstant("class", cst);
         if(ev == null) {
            throw new IllegalStateException("Unable to create enum");
         }
         Value result = ev.evaluate(scope, s);              
         Scope instance = result.getValue();
//         instance.setAttribute("identity", new Constant(title));         
         instance.addConstant("name", new Constant(name, "name"));
         instance.addConstant("ordinal", new Constant(index, "ordinal"));            
         String constraint=tp.getName();
         Constant literal = new Constant(instance,name);
         scope.addConstant(name, literal);
         return new Result(ResultFlow.NORMAL,instance);
      }
   }
}