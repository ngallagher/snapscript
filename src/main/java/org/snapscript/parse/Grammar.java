package org.snapscript.parse;

public interface Grammar {   
   boolean read(SyntaxReader reader, int depth);// this should be optimistic
}

