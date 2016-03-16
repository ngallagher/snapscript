package org.snapscript.engine.http.loader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class RemoteProcessBuilder {
   
   public static final String LAUNCHER_CLASS = "/org/snapscript/engine/http/loader/RemoteProcessLauncher.class";
   public static final String LOADER_CLASS = "/org/snapscript/engine/http/loader/RemoteClassLoader.class";
   public static final String TEMP_PATH = ".temp";
   
   private final ClassResourceLoader loader;
   private final File directory;
   
   public RemoteProcessBuilder(ClassResourceLoader loader, File root) {
      this.directory = new File(root, TEMP_PATH);
      this.loader = loader;
   }
   
   public void create() throws Exception {
      create(LAUNCHER_CLASS);
      create(LOADER_CLASS);
   }
   
   private void create(String path) throws Exception {
      File file = new File(directory, path);
      
      if(file.exists()) {
         file.delete();
      }
      byte[] data = loader.loadClass(path);
      
      if(data == null) {
         throw new IllegalStateException("Could not create launcher " + file);
      }
      File parent = file.getParentFile();
      
      if(!parent.exists()) {
         parent.mkdirs();
      }
      OutputStream stream = new FileOutputStream(file);
      
      try {
         stream.write(data);
      } finally {
         stream.close();
      }
   }
}
