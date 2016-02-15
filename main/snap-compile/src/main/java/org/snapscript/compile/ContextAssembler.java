package org.snapscript.compile;

import java.util.List;

import org.snapscript.compile.instruction.Instruction;
import org.snapscript.compile.instruction.InstructionBuilder;
import org.snapscript.compile.instruction.Code;
import org.snapscript.compile.instruction.CodeResolver;
import org.snapscript.core.Context;
import org.snapscript.core.InternalStateException;
import org.snapscript.core.Type;
import org.snapscript.parse.Line;
import org.snapscript.parse.SyntaxNode;
import org.snapscript.parse.Token;

public class ContextAssembler implements Assembler {
   
   private final InstructionBuilder builder;
   private final CodeResolver resolver;
   private final Object[] empty;

   public ContextAssembler(Context context) {
      this.builder = new InstructionBuilder(context);
      this.resolver = new CodeResolver(context);
      this.empty = new Object[]{};
   }
   
   @Override
   public <T> T assemble(SyntaxNode token, String name) throws Exception {
      return (T)create(token, name, 0);
   }
   
   private Object create(SyntaxNode node, String name, int depth) throws Exception {
      List<SyntaxNode> children = node.getNodes();
      String grammar = node.getGrammar();
      Code type = resolver.resolve(grammar);
      int size = children.size();
      
      if (type == null) {
         return createChild(node, name, children, type,depth);
      }
      if (size > 0) {
         return createBranch(node, name, children, type,depth);
      }
      return createLeaf(node, name, children, type,depth);
   }
   
   private Object createBranch(SyntaxNode node, String name, List<SyntaxNode> children, Code code, int depth) throws Exception {
      Instruction instruction = code.getInstruction();
      Type type = code.getType();
      Line line = node.getLine();
      int size = children.size();
      
      if(size > 0) {
         Object[] arguments = new Object[size];
   
         for (int i = 0; i < size; i++) {
            SyntaxNode child = children.get(i);
            Object argument = create(child, name, depth+1);
            
            arguments[i] = argument;
         }
         return builder.create(type, arguments, line, instruction.trace);
      }
      return builder.create(type, empty, line, instruction.trace);
   }

   private Object createChild(SyntaxNode node, String name, List<SyntaxNode> children, Code code, int depth) throws Exception {
      String grammar = node.getGrammar();
      int size = children.size();
      
      if (size > 1) {
         throw new InternalStateException("No type defined for " + grammar);
      }
      if (size > 0) {
         SyntaxNode child = children.get(0);

         if (child == null) {
            throw new InternalStateException("No child for " + grammar);
         }
         return create(child, name, depth);
      }
      if (size > 0) {
         return createBranch(node, name, children, code, depth);
      }
      return createLeaf(node, name, children, code, depth);
   }
   
   private Object createLeaf(SyntaxNode node, String name, List<SyntaxNode> children, Code code, int depth) throws Exception {
      Token token = node.getToken();     
      Line line = node.getLine();
      
      if (code != null) {
         Instruction instruction = code.getInstruction();
         Type type = code.getType();
         
         if (token == null) {
            return builder.create(type, empty, line, instruction.trace); // no line number????
         }      
         return builder.create(type, new Object[]{token}, line, instruction.trace);
      }
      return token;
   }
}
