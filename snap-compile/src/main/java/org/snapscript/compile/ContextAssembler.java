package org.snapscript.compile;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.snapscript.compile.instruction.Instruction;
import org.snapscript.compile.instruction.InstructionBuilder;
import org.snapscript.core.Bug;
import org.snapscript.core.Context;
import org.snapscript.core.Type;
import org.snapscript.core.TypeLoader;
import org.snapscript.parse.Line;
import org.snapscript.parse.SyntaxNode;
import org.snapscript.parse.Token;

public class ContextAssembler implements Assembler {
   
   private final Map<String, Instruction> codes;
   private final Map<String, Operation> types;
   private final InstructionBuilder builder;
   private final Context context;
   private final Object[] empty;

   public ContextAssembler(Context context) {
      this.codes = new LinkedHashMap<String, Instruction>();
      this.types = new LinkedHashMap<String, Operation>();
      this.builder = new InstructionBuilder(context);
      this.empty = new Object[]{};
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
            types.put(id, new Operation(instruction, type));
         }  
      } 
      return (T)create(token, name, 0);
   }
   
   private Object create(SyntaxNode node, String name, int depth) throws Exception {
      List<SyntaxNode> children = node.getNodes();
      String grammar = node.getGrammar();
      Operation type = types.get(grammar);
      int size = children.size();
      
      if (type == null) {
         return createChild(node, name, children, type,depth);
      }
      if (size > 0) {
         return createBranch(node, name, children, type,depth);
      }
      return createLeaf(node, name, children, type,depth);
   }
   
   private Object createBranch(SyntaxNode node, String name, List<SyntaxNode> children, Operation type,int depth) throws Exception {
      TypeLoader loader = context.getLoader();
      Line line = node.getLine();
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
      return builder.create(type.type, arguments, line, type.instruction.trace);
   }

   // this is essentially a skip of an unknown node!!
   private Object createChild(SyntaxNode node, String name, List<SyntaxNode> children, Operation type, int depth) throws Exception {
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
   
   private Object createLeaf(SyntaxNode node, String name, List<SyntaxNode> children, Operation type, int depth) throws Exception {
      Token token = node.getToken();     
      Line line = node.getLine();
      
      if (type != null) {
         if (token == null) {
            return builder.create(type.type, empty, line, type.instruction.trace); // no line number????
         }      
         return builder.create(type.type, new Object[]{token}, line, type.instruction.trace);
      }
      return token;
   }
   
   @Bug("This needs ot be a proper type, InstructionBuilder -> OperationBuilder")
   public static class Operation{
      
      private final Instruction instruction;
      private final Type type;
      
      public Operation(Instruction instruction, Type type) {
         this.instruction = instruction;
         this.type = type;
      }
   }
}
