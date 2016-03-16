package org.snapscript.engine.http.loader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class RemoteProcessBuilder {
   
   public static final String CLASS_PATH = "/org/snapscript/engine/http/loader/RemoteProcessLauncher.class";
   public static final String TEMP_PATH = ".temp";
   
   private final ClassResourceLoader loader;
   private final File directory;
   
   public RemoteProcessBuilder(ClassResourceLoader loader, File root) {
      this.directory = new File(root, TEMP_PATH);
      this.loader = loader;
   }
   
   public void create() {
      try {
         File file = new File(directory, CLASS_PATH);
         
         if(file.exists()) {
            file.delete();
         }
         byte[] data = loader.loadClass(CLASS_PATH);
         File parent = file.getParentFile();
         
         if(!parent.exists()) {
            parent.mkdirs();
         }
         OutputStream stream = new FileOutputStream(file);
         stream.write(data);
         stream.close();
      }catch(Exception e) {
         e.printStackTrace();
      }
   }
}
