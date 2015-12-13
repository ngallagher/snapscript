package org.snapscript.compile;

import org.snapscript.common.Cache;
import org.snapscript.common.LeastRecentlyUsedCache;
import org.snapscript.compile.instruction.Instruction;
import org.snapscript.core.Context;
import org.snapscript.core.Package;
import org.snapscript.core.PackageLinker;
import org.snapscript.core.NoLibrary;
import org.snapscript.core.Statement;
import org.snapscript.core.StatementLibrary;
import org.snapscript.parse.SyntaxCompiler;
import org.snapscript.parse.SyntaxNode;
import org.snapscript.parse.SyntaxParser;

public class ContextLinker implements PackageLinker {
   
   private final Cache<String, Statement> cache;
   private final Instruction instruction;
   private final SyntaxCompiler compiler;
   private final Assembler assembler;   
   
   public ContextLinker(Context context) {
      this(context, Instruction.SCRIPT_PACKAGE);
   }
   
   public ContextLinker(Context context, Instruction instruction) {
      this.cache = new LeastRecentlyUsedCache<String, Statement>();
      this.assembler = new ContextAssembler(context);      
      this.compiler = new SyntaxCompiler();
      this.instruction = instruction;
   }
   
   @Override
   public Package link(String resource, String source) throws Exception {
      return link(resource, source, instruction.name);
   }
   
   @Override
   public Package link(String resource, String source, String grammar) throws Exception {
      Statement linked = cache.fetch(resource);
      
      if(linked == null) {
         SyntaxParser parser = compiler.compile();
         SyntaxNode node = parser.parse(resource, source, grammar);
         Statement statement = assembler.assemble(node, resource);
         
         cache.cache(resource, statement); 
         return new StatementLibrary(statement, resource);
      }
      return new NoLibrary(); 
   } 
}
