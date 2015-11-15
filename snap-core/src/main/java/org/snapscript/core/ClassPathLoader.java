package org.snapscript.core;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class ClassPathLoader {
   
   private static final String EXTENSION = ".snap";
   
   private final ByteArrayOutputStream buffer;
   private final LibraryLinker linker;
   private final String suffix;
   private final Set libraries;
   private final byte[] chunk;

   public ClassPathLoader(LibraryLinker linker){
      this(linker, EXTENSION);
   }
   
   public ClassPathLoader(LibraryLinker linker, String suffix){
      this.libraries = new CopyOnWriteArraySet();
      this.buffer = new ByteArrayOutputStream();
      this.chunk = new byte[2048];
      this.linker = linker;
      this.suffix = suffix;
   }

   public Library load(String qualifier) throws Exception {
      if(!libraries.contains(qualifier)) {
         String path = qualifier.replace('.', '/');
         InputStream source = open(path + suffix);
         
         try {
            int count = 0;
        
            while((count = source.read(chunk)) !=- 1){
               buffer.write(chunk, 0, count);
            }
            String text = buffer.toString();
            
            try {
               return linker.link(qualifier, text);
            } catch(Exception e) {
               throw new IllegalStateException("Could not load library '" + path + suffix + "'", e);
            }
         } finally {
            libraries.add(qualifier);
            buffer.reset();
            source.close();         
         }
      }
      return new NoLibrary();
   }
   
   private InputStream open(String library) throws Exception {
      Class type = getClass();
      ClassLoader loader = type.getClassLoader();
      InputStream stream = loader.getResourceAsStream(library);
      
      if(stream == null) {
         throw new IllegalStateException("Could not find library '" + library + "'");
      }
      return stream;
   }
}
