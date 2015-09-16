package org.snapscript.core.execute;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class ClassPathLoader {
   
   private final ByteArrayOutputStream buffer;
   private final LibraryLinker linker;
   private final String suffix;
   private final byte[] chunk;

   public ClassPathLoader(LibraryLinker linker){
      this(linker, ".js");
   }
   
   public ClassPathLoader(LibraryLinker linker, String suffix){
      this.buffer = new ByteArrayOutputStream();
      this.chunk = new byte[2048];
      this.linker = linker;
      this.suffix = suffix;
   }

   public Library load(String qualifier) throws Exception {
      String text = read(qualifier);
      return linker.link(text);
   }
   
   private String read(String qualifier) throws Exception {
      String path = qualifier.replace('.', '/');
      InputStream source = open(path + suffix);
      
      try {
         int count = 0;
     
         while((count = source.read(chunk)) !=- 1){
            buffer.write(chunk, 0, count);
         }
         return buffer.toString();
      } finally {
         buffer.reset();
         source.close();         
      }
   }
   
   private InputStream open(String library) throws Exception {
      Class type = getClass();
      ClassLoader loader = type.getClassLoader();
      InputStream stream = loader.getResourceAsStream(library);
      
      if(stream == null) {
         throw new IllegalStateException("Could not find library " + library);
      }
      return stream;
   }
}
