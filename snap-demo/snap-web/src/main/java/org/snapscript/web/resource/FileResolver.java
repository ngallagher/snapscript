package org.snapscript.web.resource;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.channels.FileChannel;

public class FileResolver {

   private final FileManager manager;
   private final String index;

   public FileResolver(FileManager manager, String index) {
      this.manager = manager;
      this.index = index;
   }

   public File resolveFile(String path) throws IOException {
      if (path.equals("/") || path.endsWith("/")) {
         return manager.createFile(index);
      }
      return manager.createFile(path); 
   }

   public Reader resolveReader(String path) throws IOException {
      File file = resolveFile(path);

      if (file == null || !file.exists()) {
         throw new FileNotFoundException("Path '" + path + "' resolved to file '" + file + "' which does not exist");
      }
      return manager.openReader(file);
   }

   public InputStream resolveStream(String path) throws IOException {
      File file = resolveFile(path);

      if (file == null || !file.exists()) {
         throw new FileNotFoundException("Path '" + path + "' resolved to file '" + file + "' which does not exist");
      }
      return manager.openInputStream(file);
   }

   public FileChannel resolveChannel(String path) throws IOException {
      File file = resolveFile(path);

      if (file == null || !file.exists()) {
         throw new FileNotFoundException("Path '" + path + "' resolved to file '" + file + "' which does not exist");
      }
      return manager.openInputChannel(file);
   }
}
