package org.snapscript.compile;

import org.snapscript.common.Cache;
import org.snapscript.common.LeastRecentlyUsedCache;
import org.snapscript.core.Context;
import org.snapscript.core.Library;
import org.snapscript.core.LibraryLinker;
import org.snapscript.core.NoLibrary;
import org.snapscript.core.Statement;
import org.snapscript.core.StatementLibrary;
import org.snapscript.parse.SyntaxCompiler;
import org.snapscript.parse.SyntaxNode;
import org.snapscript.parse.SyntaxParser;

public class ContextLinker implements LibraryLinker {
   
   private final Cache<String, Statement> cache;
   private final SyntaxCompiler compiler;
   private final Assembler assembler;   
   
   public ContextLinker(Context context) {
      this.cache = new LeastRecentlyUsedCache<String, Statement>();
      this.assembler = new ContextAssembler(context);      
      this.compiler = new SyntaxCompiler();
   }
   
   @Override
   public Library link(String name, String source) throws Exception {
      return link(name, source, "library");
   }
   
   @Override
   public Library link(String name, String source, String grammar) throws Exception {
      Statement linked = cache.fetch(name);
      
      if(linked == null) {
         SyntaxParser parser = compiler.compile();
         SyntaxNode node = parser.parse(source, grammar);
         Statement statement = (Statement)assembler.assemble(node, name);
         
         cache.cache(name, statement); 
         return new StatementLibrary(statement, name);
      }
      return new NoLibrary(); 
   } 
}