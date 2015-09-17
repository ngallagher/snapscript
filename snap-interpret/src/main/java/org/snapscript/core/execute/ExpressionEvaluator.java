package org.snapscript.core.execute;

import java.util.LinkedHashMap;
import java.util.Map;

import org.snapscript.assembler.Assembler;
import org.snapscript.assembler.InstructionResolver;
import org.snapscript.common.Cache;
import org.snapscript.common.LeastRecentlyUsedCache;
import org.snapscript.core.Context;
import org.snapscript.core.Evaluator;
import org.snapscript.core.MapModel;
import org.snapscript.core.Model;
import org.snapscript.core.ModelScope;
import org.snapscript.core.Module;
import org.snapscript.core.ModuleScope;
import org.snapscript.core.Scope;
import org.snapscript.core.Value;
import org.snapscript.core.binary.BinaryGenerator;
import org.snapscript.parse.SyntaxCompiler;
import org.snapscript.parse.SyntaxNode;
import org.snapscript.parse.SyntaxParser;

public class ExpressionEvaluator implements Evaluator{
   
   private final Cache<String, Evaluation> cache;
   private final BinaryGenerator generator;
   private final SyntaxCompiler compiler;
   private final Assembler assembler;
   private final Interpretation root;
   private final Context context;
   
   public ExpressionEvaluator(InstructionResolver resolver, Context context){
      this(resolver, context, Interpretation.EXPRESSION);
   }
   
   public ExpressionEvaluator(InstructionResolver resolver, Context context, Interpretation root) {
      this.cache = new LeastRecentlyUsedCache<String, Evaluation>();
      this.generator = new BinaryGenerator(".bin");
      this.assembler = new Assembler(generator, resolver, context);
      this.compiler = new SyntaxCompiler();
      this.context = context;
      this.root = root;
   }
   
   @Override
   public <T> T evaluate(String source) throws Exception{
      Map<String, Object> map = new LinkedHashMap<String, Object>();
      Model model = new MapModel(map);
      
      return (T)evaluate(source, model);
   }
   
   @Override
   public <T> T evaluate(String source, Model model) throws Exception{
      Evaluation evaluation = cache.fetch(source);
      Module module = context.getModule();
      
      if(evaluation == null) {
         SyntaxParser parser = compiler.compile();
         SyntaxNode node = parser.parse(source, root.name);
         //LexerBuilder.print(parser, source, root.name); // Evaluating the following
         evaluation = (Evaluation)assembler.assemble(node, "xx");
         cache.cache(source, evaluation);      
      }
      Scope base = new ModuleScope(module);
      Scope scope = new ModelScope(base, model);
      
      Value reference = evaluation.evaluate(scope,null);
      return (T)reference.getValue();
      
   }
}
