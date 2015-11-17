package org.snapscript.core;

public interface PackageLinker {  
   Package link(String name, String source) throws Exception;
   Package link(String name, String source, String grammar) throws Exception;
}
