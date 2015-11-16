package org.snapscript.compile.instruction;

import java.rmi.server.Operation;

import org.snapscript.common.Cache;
import org.snapscript.common.LeastRecentlyUsedCache;
import org.snapscript.compile.assemble.Assembler;
import org.snapscript.compile.assemble.InstructionAssembler;
import org.snapscript.core.Context;
import org.snapscript.core.Evaluator;
import org.snapscript.core.Module;
import org.snapscript.core.ModuleBuilder;
import org.snapscript.core.Scope;
import org.snapscript.core.Value;
import org.snapscript.parse.SyntaxCompiler;
import org.snapscript.parse.SyntaxNode;
import org.snapscript.parse.SyntaxParser;

public class ExpressionEvaluator implements Evaluator{
   
   private final Cache<String, Evaluation> cache;
   private final SyntaxCompiler compiler;
   private final Assembler assembler;
   private final Instruction root;
   private final Context context;
   
   public ExpressionEvaluator(Context context){
      this(context, Instruction.EXPRESSION);
   }
   
   public ExpressionEvaluator(Context context, Instruction root) {
      this.cache = new LeastRecentlyUsedCache<String, Evaluation>();
      this.assembler = new InstructionAssembler(context);
      this.compiler = new SyntaxCompiler();
      this.context = context;
      this.root = root;
   }
   
   @Override
   public <T> T evaluate(String source) throws Exception{
      Evaluation evaluation = cache.fetch(source);
      ModuleBuilder builder = context.getBuilder();
      Module module = builder.resolve();
      
      if(evaluation == null) {
         SyntaxParser parser = compiler.compile();
         SyntaxNode node = parser.parse(source, root.name);
         
         evaluation = (Evaluation)assembler.assemble(node, source);
         cache.cache(source, evaluation);      
      }
      Scope base = module.getScope();
      Scope scope = base.getScope();
      
      Value reference = evaluation.evaluate(scope,null);
      return (T)reference.getValue();
      
   }
}
