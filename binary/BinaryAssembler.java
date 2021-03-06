package com.zuooh.script.core.binary;

import java.io.FileInputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.Callable;

import com.zuooh.script.core.Context;
import com.zuooh.script.core.Type;
import com.zuooh.script.core.TypeLoader;
import com.zuooh.script.core.convert.FunctionBinder;
import com.zuooh.script.core.execute.Instruction;
import com.zuooh.script.core.execute.Result;
import com.zuooh.script.parse.NumberToken;
import com.zuooh.script.parse.StringToken;

public class BinaryAssembler {
   
   private final Map<String, Instruction> codes;
   private final Map<String, Type> types;
   private final Context context;
   private final String extension;

   public BinaryAssembler(Context context, String extension) {
      this.codes = new LinkedHashMap<String, Instruction>();
      this.types = new LinkedHashMap<String, Type>();
      this.extension = extension;
      this.context = context;
   }

   public Object assemble(String name) throws Exception {
      if(types.isEmpty()) {
         Instruction[] list = Instruction.values();         
      
         for(Instruction instruction :list){
            TypeLoader loader = context.getLoader();
            Type type = loader.load(instruction.type);
            
            codes.put(instruction.name, instruction);
            types.put(instruction.name, type);
         }  
      }
      return build(name);
   }
   
   public Object build(String name)throws Exception{
      FileInputStream stream = new FileInputStream(name +"."+extension);
      OperationReader reader = new OperationReader(stream);
      Stack<Operation> stack=new Stack<Operation>();
      Stack<Object> done=new Stack<Object>();
      
      try {
         Operation begin = reader.read();
         Operation version = reader.read();
         
         if(begin.code !=OperationCode.BEGIN) {
            throw new IllegalStateException("Binary of '" + name +"' is corrupted");
         }
         if(version.code !=OperationCode.VERSION){
            throw new IllegalStateException("Binary of '" + name +"' is corrupted");
         }
         Operation op = reader.read();
         
         while(op != null) {
            if(op.code ==OperationCode.END){
               return done.pop();
            }
            if(op.code==OperationCode.PUSH){
               stack.push(op);
            }else if(op.code==OperationCode.POP){
               FunctionBinder binder = context.getBinder();
               TypeLoader loader = context.getLoader();
               Instruction instruction=op.instruction;
               Operation top=stack.pop();
               int size =op.count;
               if(top.instruction!=instruction){
                  throw new IllegalStateException("Invalid instruction");
               }
               Object[] arguments = new Object[size];
               Type[] parameters = new Type[size];
   
               for (int i = size - 1; i >= 0; i--) {
                  Object argument = done.pop();
                  Class parameter = argument.getClass();
                  Type type = loader.load(parameter);
                  
                  arguments[i] = argument;
                  parameters[i] = type;
               }  
               Type type = loader.load(instruction.type);
               Callable<Result> callable = binder.bind(null, type, "new", arguments);
//               Function factory = mapper.match(type, "new", parameters);
//   
//               if (factory == null) {
//                  throw new IllegalStateException("No constructor for " + type);
//               }
//               Invocation invocation = factory.getInvocation();
//               Result result = invocation.invoke(null, null, arguments);            
               Result result = callable.call();
               Object value = result.getValue();            
               
               done.push(value);
            }else if(op.code==OperationCode.NONE){
               FunctionBinder binder = context.getBinder();
               TypeLoader loader = context.getLoader();
               Instruction instruction=op.instruction;
               Type type = loader.load(instruction.type);
               Callable<Result> callable = binder.bind(null, type, "new");
//               Function factory = mapper.match(type, "new");
//   
//               if (factory == null) {
//                  throw new IllegalStateException("No constructor for " + type);
//               }
//               Invocation invocation = factory.getInvocation();
//               Result result = invocation.invoke(null, null);     
               Result result = callable.call();
               Object value = result.getValue();            
               
               done.push(value);
            }else if(op.code==OperationCode.DOUBLE){
               done.push(new NumberToken((Double)op.value));
            }else if(op.code==OperationCode.FLOAT){
               done.push(new NumberToken((Float)op.value));
            }else if(op.code==OperationCode.INTEGER){
               done.push(new NumberToken((Integer)op.value));
            }else if(op.code==OperationCode.LONG){
               done.push(new NumberToken((Long)op.value));
            }else if(op.code==OperationCode.SHORT){
               done.push(new NumberToken((Short)op.value));
            }else if(op.code==OperationCode.OCTET){               
               done.push(new NumberToken((Byte)op.value));
            }else if(op.code==OperationCode.BOOLEAN){
               throw new IllegalStateException("Boolean not allowed");
            }else if(op.code==OperationCode.CHARACTER){
               throw new IllegalStateException("Character not allowed");
            }else if(op.code==OperationCode.TEXT){
               done.push(new StringToken((String)op.value));
            }else {
               throw new IllegalStateException("Unknown operation");
            }  
            op = reader.read();
         }
         return done.pop();
      }catch(Exception e) {
         throw new IllegalStateException("Could not read '" + name + "'", e);
      } finally {
         reader.close();
      }
   }
}
