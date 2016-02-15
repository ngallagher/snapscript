package org.snapscript.compile;

import static org.snapscript.compile.instruction.Instruction.EXPRESSION;

import org.snapscript.common.Cache;
import org.snapscript.common.LeastRecentlyUsedCache;
import org.snapscript.compile.instruction.Instruction;
import org.snapscript.core.Context;
import org.snapscript.core.Evaluation;
import org.snapscript.core.ExpressionExecutor;
import org.snapscript.core.InternalStateException;
import org.snapscript.core.Scope;
import org.snapscript.core.Value;
import org.snapscript.parse.SyntaxCompiler;
import org.snapscript.parse.SyntaxNode;
import org.snapscript.parse.SyntaxParser;

public class ContextExecutor implements ExpressionExecutor {
   
   private final Cache<String, Evaluation> cache;
   private final SyntaxCompiler compiler;
   private final Instruction instruction;
   private final Assembler assembler;
   
   public ContextExecutor(Context context){
      this(context, EXPRESSION);
   }
   
   public ContextExecutor(Context context, Instruction instruction) {
      this.cache = new LeastRecentlyUsedCache<String, Evaluation>();
      this.assembler = new ContextAssembler(context);
      this.compiler = new SyntaxCompiler();
      this.instruction = instruction;
   }
   
   @Override
   public <T> T execute(Scope scope, String module, String source) throws Exception{
      Evaluation evaluation = cache.fetch(source);
      
      try {
         if(evaluation == null) {
            SyntaxParser parser = compiler.compile();
            SyntaxNode node = parser.parse(module, source, instruction.name);
            
            evaluation = assembler.assemble(node, source);
            cache.cache(source, evaluation);      
         }
         Value reference = evaluation.evaluate(scope,null);
         
         return (T)reference.getValue();
      } catch(Exception e) {
         throw new InternalStateException("Could not evaluate '" + source + "'", e);
      }
   }
}
