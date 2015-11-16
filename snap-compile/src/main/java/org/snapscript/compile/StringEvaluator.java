package org.snapscript.compile;

import org.snapscript.common.Cache;
import org.snapscript.common.LeastRecentlyUsedCache;
import org.snapscript.compile.instruction.Evaluation;
import org.snapscript.compile.instruction.Instruction;
import org.snapscript.core.Context;
import org.snapscript.core.Module;
import org.snapscript.core.ModuleBuilder;
import org.snapscript.core.Scope;
import org.snapscript.core.Value;
import org.snapscript.parse.SyntaxCompiler;
import org.snapscript.parse.SyntaxNode;
import org.snapscript.parse.SyntaxParser;

public class StringEvaluator implements Evaluator{
   
   private final Cache<String, Evaluation> cache;
   private final SyntaxCompiler compiler;
   private final Instruction instruction;
   private final Assembler assembler;
   private final Context context;
   
   public StringEvaluator(Context context){
      this(context, Instruction.EXPRESSION);
   }
   
   public StringEvaluator(Context context, Instruction instruction) {
      this.cache = new LeastRecentlyUsedCache<String, Evaluation>();
      this.assembler = new InstructionAssembler(context);
      this.compiler = new SyntaxCompiler();
      this.context = context;
      this.instruction = instruction;
   }
   
   @Override
   public <T> T evaluate(String source) throws Exception{
      Evaluation evaluation = cache.fetch(source);
      ModuleBuilder builder = context.getBuilder();
      Module module = builder.resolve();
      
      if(evaluation == null) {
         SyntaxParser parser = compiler.compile();
         SyntaxNode node = parser.parse(source, instruction.name);
         
         evaluation = (Evaluation)assembler.assemble(node, source);
         cache.cache(source, evaluation);      
      }
      Scope base = module.getScope();
      Scope scope = base.getScope();
      
      Value reference = evaluation.evaluate(scope,null);
      return (T)reference.getValue();
      
   }
}
