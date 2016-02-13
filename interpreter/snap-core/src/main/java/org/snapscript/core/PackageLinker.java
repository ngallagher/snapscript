package org.snapscript.core;

public interface PackageLinker {  
   Package link(String resource, String source) throws Exception;
   Package link(String resource, String source, String grammar) throws Exception;
}
