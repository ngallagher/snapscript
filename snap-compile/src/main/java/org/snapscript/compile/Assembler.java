package org.snapscript.compile;

import org.snapscript.parse.SyntaxNode;

public interface Assembler {
   Object assemble(SyntaxNode token, String name) throws Exception;
}
