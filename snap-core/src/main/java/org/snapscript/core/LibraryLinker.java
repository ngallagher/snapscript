package org.snapscript.core;

public interface LibraryLinker {  
   Library link(String source) throws Exception;
   Library link(String source, String grammar) throws Exception;
}
