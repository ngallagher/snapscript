package org.snapscript.core.execute;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import org.snapscript.core.Context;
import org.snapscript.core.Type;
import org.snapscript.core.TypeLoader;
import org.snapscript.core.binary.BinaryAssembler;
import org.snapscript.core.binary.OperationCode;
import org.snapscript.core.convert.FunctionBinder;
import org.snapscript.parse.SyntaxNode;
import org.snapscript.parse.Token;

public class Assembler {
   
   private final Map<String, Instruction> codes;
   private final Map<String, Type> types;
   private final AssemblerListener listener;
   private final BinaryAssembler assembler;
   private final Context context;

   public Assembler(AssemblerListener listener, Context context) {
      this.codes = new LinkedHashMap<String, Instruction>();
      this.types = new LinkedHashMap<String, Type>();
      this.assembler = new BinaryAssembler(context, ".bin");
      this.listener = listener;
      this.context = context;
   }

   public Object assemble(SyntaxNode token, String name) throws Exception {
      long start=System.currentTimeMillis();
      if(types.isEmpty()) {
         Instruction[] list = Instruction.values();         
      
         for(Instruction instruction :list){
            TypeLoader loader = context.getLoader();
            Type type = loader.load(instruction.type);
            
            codes.put(instruction.name, instruction);
            types.put(instruction.name, type);
         }  
      } 
      Object result = create(token, name, 0);
      long finish=System.currentTimeMillis();
      long normal=finish-start;
      System.err.println("Assembly time  took "+normal);
      listener.assemble(name, OperationCode.END, Instruction.SCRIPT, 0, null);
//      try{
//
//         start = System.currentTimeMillis();
//         Object object = assembler.assemble(name);
//         finish=System.currentTimeMillis();
//         long time=finish-start;
//         System.err.println("Binary assemble time was "+time+" normal was "+normal);
//         if(object != null){
//            return object;
//         }
//      }catch(Exception e){
//         e.printStackTrace();
//      }
      return result;
   }
   
   private Object create(SyntaxNode node, String name, int depth) throws Exception {
      List<SyntaxNode> children = node.getNodes();
      String grammar = node.getGrammar();
      Type type = types.get(grammar);
      int size = children.size();
      
      if (type == null) {
         return createChild(node, name, children, type,depth);
      }
      if (size > 0) {
         return createBranch(node, name, children, type,depth);
      }
      return createLeaf(node, name, children, type,depth);
   }
   
   private Object createBranch(SyntaxNode node, String name, List<SyntaxNode> children, Type type,int depth) throws Exception {
      FunctionBinder binder = context.getBinder();
      TypeLoader loader = context.getLoader();
      String grammar = node.getGrammar();
      Instruction instruction = codes.get(grammar);
      int size = children.size();
      
      Object[] arguments = new Object[size];
      Type[] parameters = new Type[size];

      listener.assemble(name, OperationCode.PUSH, instruction, size, null);  

      for (int i = 0; i < size; i++) {
         SyntaxNode child = children.get(i);
         Object argument = create(child, name, depth+1);
         Class parameter = argument.getClass();
         Type t = loader.load(parameter);
         
         if(argument.equals(new Integer(70))) {
            System.err.println();
         }
         arguments[i] = argument;
         parameters[i] = t;
      }
      listener.assemble(name, OperationCode.POP, instruction, size, null);  

      Callable<Result> callable = binder.bind(null, type, "new", arguments);
      Result result = callable.call();
      return result.getValue();
   }

   // this is essentially a skip of an unknown node!!
   private Object createChild(SyntaxNode node, String name, List<SyntaxNode> children, Type type, int depth) throws Exception {
      String grammar = node.getGrammar();
      int size = children.size();
      
      if (size > 1) {
         throw new IllegalStateException("No type defined for " + grammar);
      }
      if (size > 0) {
         SyntaxNode child = children.get(0);

         if (child == null) {
            throw new IllegalStateException("No child for " + grammar);
         }
         return create(child, name, depth);
      }
      if (size > 0) {
         return createBranch(node, name, children, type,depth);
      }
      return createLeaf(node, name, children, type,depth);
   }
   
   private Object createLeaf(SyntaxNode node, String name, List<SyntaxNode> children, Type type, int depth) throws Exception {
      FunctionBinder binder = context.getBinder();
      String grammar = node.getGrammar();
      Instruction instruction = codes.get(grammar);      
      Token token = node.getToken();      

      if (type != null) {
         if (token == null) {
            Callable<Result> callable = binder.bind(null, type, "new");
            Result result = callable.call();
            listener.assemble(name, OperationCode.NONE, instruction, 0, null);      
            return result.getValue();
         }      
         Callable<Result> callable = binder.bind(null, type, "new", token);    
         Result result = callable.call();
         Object value = token.getValue();

         listener.assemble(name, OperationCode.PUSH, instruction, 1, null);          
         listener.assemble(name, OperationCode.resolveCode(value), instruction,1,value);
         listener.assemble(name, OperationCode.POP, instruction, 1, null);  
 
         return result.getValue();
      }
      Object value = token.getValue();
      listener.assemble(name, OperationCode.resolveCode(value), instruction,1,value);
      return token;
   }
}