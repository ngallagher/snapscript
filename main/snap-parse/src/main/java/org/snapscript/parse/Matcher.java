package org.snapscript.parse;

public interface Matcher {
   boolean match(SyntaxReader reader, int depth);
}
