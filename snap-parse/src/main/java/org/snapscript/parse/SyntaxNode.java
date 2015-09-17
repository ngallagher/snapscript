package org.snapscript.parse;

import java.util.List;

public interface SyntaxNode {
   List<SyntaxNode> getNodes();
   String getSource();
   String getGrammar();
   Token getToken();
   int getStart();
   int getDepth();
   int getLine();
}
