package org.snapscript.core.execute;

import java.util.LinkedHashMap;
import java.util.Map;

import org.snapscript.Evaluator;
import org.snapscript.Model;
import org.snapscript.common.Cache;
import org.snapscript.common.LeastRecentlyUsedCache;
import org.snapscript.core.Context;
import org.snapscript.core.MapModel;
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
   private final Instruction root;
   private final Context context;
   
   public ExpressionEvaluator(Context context){
      this(context, Instruction.EXPRESSION);
   }
   
   public ExpressionEvaluator(Context context, Instruction root) {
      this.cache = new LeastRecentlyUsedCache<String, Evaluation>();
      this.generator = new BinaryGenerator(".bin");
      this.assembler = new Assembler(generator, context);
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
