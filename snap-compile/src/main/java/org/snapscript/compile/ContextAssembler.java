package org.snapscript.compile;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import org.snapscript.compile.instruction.Instruction;
import org.snapscript.core.Context;
import org.snapscript.core.Result;
import org.snapscript.core.Type;
import org.snapscript.core.TypeLoader;
import org.snapscript.core.bind.FunctionBinder;
import org.snapscript.parse.SyntaxNode;
import org.snapscript.parse.Token;

public class ContextAssembler implements Assembler {
   
   private final Map<String, Instruction> codes;
   private final Map<String, Type> types;
   private final Context context;

   public ContextAssembler(Context context) {
      this.codes = new LinkedHashMap<String, Instruction>();
      this.types = new LinkedHashMap<String, Type>();
      this.context = context;
   }
   
   @Override
   public <T> T assemble(SyntaxNode token, String name) throws Exception {
      if(types.isEmpty()) {
         Instruction[] list = Instruction.values();       
      
         for(Instruction instruction :list){
            TypeLoader loader = context.getLoader();
            Class value = instruction.getType();
            String id = instruction.getName();
            Type type = loader.loadType(value);
            
            codes.put(id, instruction);
            types.put(id, type);
         }  
      } 
      return (T)create(token, name, 0);
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
      int size = children.size();
      
      Object[] arguments = new Object[size];
      Type[] parameters = new Type[size];

      for (int i = 0; i < size; i++) {
         SyntaxNode child = children.get(i);
         Object argument = create(child, name, depth+1);
         Class parameter = argument.getClass();
         Type t = loader.loadType(parameter);
         
         arguments[i] = argument;
         parameters[i] = t;
      }
      Callable<Result> callable = binder.bind(null, type, "new", arguments);
      if(callable == null) {
         throw new IllegalStateException("No constructor for " + type);
      }
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
      Token token = node.getToken();      

      if (type != null) {
         if (token == null) {
            Callable<Result> callable = binder.bind(null, type, "new");
            Result result = callable.call();
            return result.getValue();
         }      
         Callable<Result> callable = binder.bind(null, type, "new", token);    
         
         if(callable == null) {
            throw new IllegalStateException("No constructor for " + type);
         }
         Result result = callable.call();
         Object value = token.getValue();
 
         return result.getValue();
      }
      return token;
   }
}
