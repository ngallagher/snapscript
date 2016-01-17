package org.snapscript.engine.http.resource;

import static java.nio.file.StandardOpenOption.READ;
import static java.nio.file.StandardOpenOption.WRITE;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileManager {

   private final String encoding;
   private final File base;

   public FileManager(File base) {
      this(base, "UTF-8");
   }
   
   public FileManager(File base, String encoding) {
      this.encoding = encoding;
      this.base = base;
   }

   public File createFile(String path) throws IOException {
      String normal = path.replace('/', File.separatorChar);
      String trimmed = normal.trim();

      return new File(base, trimmed);
   }

   public InputStream openInputStream(String path) throws IOException {
      File file = createFile(path);

      if (!file.exists()) {
         throw new FileNotFoundException("Could not find file " + file + " from " + path);
      }
      return openInputStream(file);
   }

   public InputStream openInputStream(File file) throws IOException {
      return new FileInputStream(file);
   }

   public Reader openReader(String path) throws IOException {
      File file = createFile(path);

      if (!file.exists()) {
         throw new FileNotFoundException("Could not find file " + file + " from " + path);
      }
      return openReader(file);
   }

   public Reader openReader(File file) throws IOException {
      InputStream source = openInputStream(file);
      
      if(encoding != null) {
         return new InputStreamReader(source, encoding);
      }
      return new InputStreamReader(source);
   }

   public FileChannel openInputChannel(String path) throws IOException {
      File file = createFile(path);

      if (!file.exists()) {
         throw new FileNotFoundException("Could not find file " + file + " from " + path);
      }
      return openInputChannel(file);
   }

   public FileChannel openInputChannel(File file) throws IOException {
      String location = file.getCanonicalPath();
      Path path = Paths.get(location);
      
      if (!file.exists()) {
         throw new FileNotFoundException("Could not find file " + file);
      }
      return FileChannel.open(path, READ);
   }

   public OutputStream openOutputStream(String path) throws IOException {
      File file = createFile(path);
      File directory = file.getParentFile();

      if (!directory.exists()) {
         directory.mkdirs();
      }
      return openOutputStream(file);
   }

   public OutputStream openOutputStream(File file) throws IOException {
      File directory = file.getParentFile();

      if (!directory.exists()) {
         directory.mkdirs();
      }
      return new FileOutputStream(file);
   }

   public FileChannel openOutputChannel(String path) throws IOException {
      File file = createFile(path);
      File directory = file.getParentFile();

      if (!directory.exists()) {
         directory.mkdirs();
      }
      return openOutputChannel(file);
   }

   public FileChannel openOutputChannel(File file) throws IOException {
      String location = file.getCanonicalPath();
      Path path = Paths.get(location);
      
      return FileChannel.open(path, WRITE);
   }
}
