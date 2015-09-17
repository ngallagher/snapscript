package org.snapscript.assembler;

import org.snapscript.common.Cache;
import org.snapscript.common.LeastRecentlyUsedCache;
import org.snapscript.core.Context;
import org.snapscript.core.Library;
import org.snapscript.core.LibraryLinker;
import org.snapscript.core.Statement;
import org.snapscript.core.binary.BinaryGenerator;
import org.snapscript.parse.SyntaxCompiler;
import org.snapscript.parse.SyntaxNode;
import org.snapscript.parse.SyntaxParser;

public class ScriptLinker implements LibraryLinker {
   
   private final Cache<String, Statement> cache;
   private final BinaryGenerator generator;
   private final SyntaxCompiler compiler;
   private final Assembler assembler;
   private final String root;   
   
   public ScriptLinker(InstructionResolver resolver, Context context) {
      this(resolver, context, "script");
   }   
   
   public ScriptLinker(InstructionResolver resolver, Context context, String root) {
      this.cache = new LeastRecentlyUsedCache<String, Statement>();
      this.generator = new BinaryGenerator(".bin");
      this.assembler = new Assembler(generator, resolver, context);      
      this.compiler = new SyntaxCompiler();
      this.root = root;
   }
   
   @Override
   public Library link(String source) throws Exception {
      Statement linked = cache.fetch(source);
      
      if(linked == null) {
         SyntaxParser parser = compiler.compile();
         SyntaxNode node = parser.parse(source, root);
         Script statement = (Script)assembler.assemble(node, "xx");
         
         cache.cache(source, statement); 
         return new ScriptLibrary(statement);
      }
      return new ScriptLibrary();
   } 
}
