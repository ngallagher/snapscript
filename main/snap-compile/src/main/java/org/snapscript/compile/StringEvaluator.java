package org.snapscript.compile;

import static org.snapscript.compile.instruction.Instruction.EXPRESSION;
import static org.snapscript.core.Reserved.DEFAULT_PACKAGE;

import org.snapscript.common.Cache;
import org.snapscript.common.LeastRecentlyUsedCache;
import org.snapscript.compile.instruction.Instruction;
import org.snapscript.core.Context;
import org.snapscript.core.EmptyModel;
import org.snapscript.core.Evaluation;
import org.snapscript.core.Model;
import org.snapscript.core.Scope;
import org.snapscript.core.ScopeMerger;
import org.snapscript.core.Value;
import org.snapscript.parse.SyntaxCompiler;
import org.snapscript.parse.SyntaxNode;
import org.snapscript.parse.SyntaxParser;

public class StringEvaluator implements Evaluator{
   
   private final Cache<String, Evaluation> cache;
   private final SyntaxCompiler compiler;
   private final Instruction instruction;
   private final Assembler assembler;
   private final ScopeMerger merger; 
   private final String module;
   private final Model model;
   
   public StringEvaluator(Context context){
      this(context, EXPRESSION);
   }
   
   public StringEvaluator(Context context, Instruction instruction) {
      this(context, instruction, DEFAULT_PACKAGE);
   }
   
   public StringEvaluator(Context context, Instruction instruction, String module) {
      this.cache = new LeastRecentlyUsedCache<String, Evaluation>();
      this.assembler = new ContextAssembler(context);
      this.merger = new ScopeMerger(context, module);
      this.compiler = new SyntaxCompiler();
      this.model = new EmptyModel();
      this.instruction = instruction;
      this.module = module;
   }
   
   @Override
   public <T> T evaluate(String source) throws Exception{
      return evaluate(source, model);
   }
   
   @Override
   public <T> T evaluate(String source, Model model) throws Exception{
      Evaluation evaluation = cache.fetch(source);
      
      if(evaluation == null) {
         SyntaxParser parser = compiler.compile();
         SyntaxNode node = parser.parse(module, source, instruction.name);
         
         evaluation = assembler.assemble(node, source);
         cache.cache(source, evaluation);      
      }
      Scope scope = merger.merge(model);
      Value reference = evaluation.evaluate(scope,null);
      
      return (T)reference.getValue();
      
   }
}
