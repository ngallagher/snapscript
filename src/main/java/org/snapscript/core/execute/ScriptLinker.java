package org.snapscript.core.execute;

import org.snapscript.common.Cache;
import org.snapscript.common.LeastRecentlyUsedCache;
import org.snapscript.core.Context;
import org.snapscript.core.binary.BinaryGenerator;
import org.snapscript.parse.SyntaxCompiler;
import org.snapscript.parse.SyntaxNode;
import org.snapscript.parse.SyntaxParser;

public class ScriptLinker implements LibraryLinker {
   
   private final Cache<String, Statement> cache;
   private final BinaryGenerator generator;
   private final SyntaxCompiler compiler;
   private final Assembler assembler;
   private final Instruction root;   
   
   public ScriptLinker(Context context) {
      this(context, Instruction.SCRIPT);
   }   
   
   public ScriptLinker(Context context, Instruction root) {
      this.cache = new LeastRecentlyUsedCache<String, Statement>();
      this.generator = new BinaryGenerator(".bin");
      this.assembler = new Assembler(generator, context);      
      this.compiler = new SyntaxCompiler();
      this.root = root;
   }
   
   @Override
   public Library link(String source) throws Exception {
      Statement linked = cache.fetch(source);
      
      if(linked == null) {
         SyntaxParser parser = compiler.compile();
         SyntaxNode node = parser.parse(source, root.name);
         Script statement = (Script)assembler.assemble(node, "xx");
         
         cache.cache(source, statement); 
         return new ScriptLibrary(statement);
      }
      return new ScriptLibrary();
   } 
}
