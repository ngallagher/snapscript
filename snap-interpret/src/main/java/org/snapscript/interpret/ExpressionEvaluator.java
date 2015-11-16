package org.snapscript.interpret;

import org.snapscript.assemble.Assembler;
import org.snapscript.assemble.InstructionAssembler;
import org.snapscript.assemble.InstructionResolver;
import org.snapscript.common.Cache;
import org.snapscript.common.LeastRecentlyUsedCache;
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
   private final Interpretation root;
   private final Context context;
   
   public ExpressionEvaluator(InstructionResolver resolver, Context context){
      this(resolver, context, Interpretation.EXPRESSION);
   }
   
   public ExpressionEvaluator(InstructionResolver resolver, Context context, Interpretation root) {
      this.cache = new LeastRecentlyUsedCache<String, Evaluation>();
      this.assembler = new InstructionAssembler(resolver, context);
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
         //LexerBuilder.print(parser, source, root.name); // Evaluating the following
         evaluation = (Evaluation)assembler.assemble(node, "xx");
         cache.cache(source, evaluation);      
      }
      Scope base = module.getScope();
      Scope scope = base.getScope();
      
      Value reference = evaluation.evaluate(scope,null);
      return (T)reference.getValue();
      
   }
}
