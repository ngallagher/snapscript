package org.snapscript.ide;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.SelectableChannel;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.simpleframework.http.Part;
import org.simpleframework.http.Path;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.simpleframework.http.Status;
import org.simpleframework.http.core.Container;
import org.simpleframework.http.core.ContainerSocketProcessor;
import org.simpleframework.transport.SocketProcessor;
import org.simpleframework.transport.connect.Connection;
import org.simpleframework.transport.connect.SocketConnection;
import org.simpleframework.transport.trace.Trace;
import org.simpleframework.transport.trace.TraceAnalyzer;
import org.snapscript.common.LeastRecentlyUsedMap;

public class ScriptFileServer implements Container {

   private static final int LOG_EVENT_CAPACITY = 5000;

   private final Map<String, LogConsole> consoles;
   private final File rootPath;
   private final SocketAddress address;
   private final Connection connection;
   private final SocketProcessor server;
   private final TraceAnalyzer agent;
   private final File tempPath;

   public ScriptFileServer(File rootPath, File tempPath, int port) throws IOException {
      if(!rootPath.exists()) {
         throw new IllegalStateException("Root image source path must exist");
      }
      this.agent = new LogAgent();
      this.consoles = Collections.synchronizedMap(new LeastRecentlyUsedMap<String, LogConsole>());
      this.server = new ContainerSocketProcessor(this, 2);
      this.address = new InetSocketAddress(port);
      this.connection = new SocketConnection(server, agent);
      this.rootPath = rootPath;
      this.tempPath = tempPath;
   }

   public void start() throws IOException {
      connection.connect(address);
   }

   @Override
   public void handle(Request req, Response resp) {
      try {
         String path = req.getPath().getPath();

         resp.setDate("Date", System.currentTimeMillis());
         resp.setValue("Server", "FileServer/1.0");
         resp.setValue("Pragma", "no-cache");

         if(path.startsWith("/upload/file")) {
            Part part = req.getPart("file");
            InputStream source = part.getInputStream();
            File fileOut = new File(rootPath, "upload" + System.currentTimeMillis() + ".dat");
            File parent = fileOut.getParentFile();

            if(!parent.exists()) {
               parent.mkdirs();
            }
            FileOutputStream fileStream = new FileOutputStream(fileOut);
            byte[] chunk = new byte[8192];
            int count = 0;

            while((count = source.read(chunk)) != -1) {
               fileStream.write(chunk, 0, count);
            }
            fileStream.flush();
            fileStream.close();
            PrintStream out = resp.getPrintStream();
            
            resp.setContentType("text/html");
            out.println("<html>");
            out.println("<head>");
            out.println("<meta name='viewport' content='width=device-width, initial-scale=1'>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<p>");
            out.println(fileOut.getAbsolutePath());
            out.println("</p>");         
            out.println("</body>");
            out.println("</html>");
         } else if(path.startsWith("/upload/form")) {
            PrintStream out = resp.getPrintStream();
            
            resp.setContentType("text/html");
            out.println("<html>");
            out.println("<head>");
            out.println("<meta name='viewport' content='width=device-width, initial-scale=1'>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<form method='POST' enctype='multipart/form-data' action='/upload/file'>");
            out.println("File to upload: <input type='file' name='file'><br>");
            out.println("<br>");
            out.println("<input type='submit' value='Upload'>");
            out.println("</form>");           
            out.println("</body>");
            out.println("</html>");
         } else if(path.startsWith("/upload/hprof")) {
            File out = new File(rootPath, path);
            File parent = out.getParentFile();

            if(!parent.exists()) {
               parent.mkdirs();
            }
            FileOutputStream fileOut = new FileOutputStream(out);
            InputStream source = req.getInputStream();
            byte[] chunk = new byte[8192];
            int count = 0;

            while((count = source.read(chunk)) != -1) {
               fileOut.write(chunk, 0, count);
            }
            fileOut.flush();
            fileOut.close();
         } else if(path.startsWith("/upload")) {
            File out = new File(rootPath, path);
            File parent = out.getParentFile();

            if(!parent.exists()) {
               parent.mkdirs();
            }
            FileOutputStream fileOut = new FileOutputStream(out);
            InputStream source = req.getInputStream();
            byte[] chunk = new byte[8192];
            int count = 0;

            while((count = source.read(chunk)) != -1) {
               fileOut.write(chunk, 0, count);
            }
            fileOut.flush();
            fileOut.close();
         } else if(path.startsWith("/clearLogs")) {
            consoles.clear();
            resp.setContentType("text/html");
            PrintStream out = resp.getPrintStream();
            out.println("<html>");
            out.println("<head>");
            out.println("<meta name='viewport' content='width=device-width, initial-scale=1'>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>OK cleared logs!!</h1><a href='/showLogs'>showLogs</a>");
            out.println("</body>");
            out.println("</html>");
            out.close();
         } else if(path.startsWith("/dumpLog")) {
            String from = req.getParameter("from");
            if(from != null) {
               LogConsole console = consoles.get(from);
               if(console == null) {
                  console = new LogConsole(from, LOG_EVENT_CAPACITY);
                  consoles.put(from, console);
               }
               String text = req.getContent();
               console.update(text);
            }
            resp.setStatus(Status.NOT_MODIFIED);
            resp.close();
         } else if(path.startsWith("/showLog")) {
            resp.setContentType("text/html");
            String from = req.getParameter("from");
            if(from != null) {
               LogConsole console = consoles.get(from);
               if(console != null) {
                  PrintStream out = resp.getPrintStream();
                  out.println(console);
               }
            } else {
               PrintStream out = resp.getPrintStream();
               Set<String> keys = consoles.keySet();
               
               out.println("<html>");
               out.println("<head>");
               out.println("<meta name='viewport' content='width=device-width, initial-scale=1'>");            
               out.println("</head>");
               out.println("<body>");
               out.println("<ul>");
               for(String key : keys){
                  LogConsole console = consoles.get(key);
                  out.println("<li><a href='showLog?from=");
                  out.println(key);
                  out.println("'>");
                  out.println(console.getCreationTime());
                  out.println("</a></li>");
               }
               out.println("</ul>");
               out.println("</body>");
               out.println("</html>");
            }
         } else {
            if(path.startsWith(".")) {
               path = path.substring(1);
            }
            File file = new File(rootPath, path);
            
            if(file.isDirectory()) {
               resp.setContentType("text/html");
               String[] list = file.list();
               PrintStream out = resp.getPrintStream();
               
               out.println("<html>");
               out.println("<head>");
               out.println("<meta name='viewport' content='width=device-width, initial-scale=1'>");            
               out.println("</head>");
               out.println("<body>");
               out.println("<a href='/clearLogs'>clearLogs</a><ul>");
               
               for(String name : list){
                  out.println("<li><a href='");
                  if(path.endsWith("/")){
                     out.println(path);
                     out.println(name);
                  } else {
                     out.println(path);
                     out.println("/");
                     out.println(name);
                  }
                  out.println("'>");
                  out.println(name);
                  out.println("</a></li>");
               }
               out.println("</ul>");
               out.println("</body>");
               out.println("</html>");
            } else {
               byte[] data = loadFile(file, path); // load from file or classpath
               if(file.getName().endsWith(".xml")) {
                  resp.setContentType("text/xml");
               } else if(file.getName().endsWith(".css")) {
                  resp.setContentType("text/css");                  
               } else if(file.getName().endsWith(".snap")) {
                  resp.setContentType("text/plain");                  
               } else if(file.getName().endsWith(".js")) {
                  resp.setContentType("application/javascript");
               } else if(file.getName().endsWith(".json")) {
                  resp.setContentType("application/json");                  
               } else if(file.getName().endsWith(".png")) {
                  resp.setContentType("image/png");
               } else if(file.getName().endsWith(".txt")) {
                  resp.setContentType("text/plain");
               } else if(file.getName().endsWith(".html")) {
                  resp.setContentType("text/html");
               } else if(file.getName().endsWith(".apk")) {
                  resp.setContentType("application/vnd.android.package-archive");
                  resp.setValue("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");
               } else {
                  resp.setContentType("application/octetstream");
               }
               resp.setValue("Connection", "close");
               resp.setContentLength(data.length);
               OutputStream out = resp.getOutputStream();
               out.write(data);
               out.flush();
               out.close();
            }
         }
      }catch(Throwable e) {
         e.printStackTrace();

         try {
            PrintStream out = resp.getPrintStream();

            resp.setStatus(Status.INTERNAL_SERVER_ERROR);
            resp.setContentType("text/plain");
            resp.setDate("Date", System.currentTimeMillis());
            e.printStackTrace(out);
         } catch(Exception ex) {
            ex.printStackTrace();
         }
      } finally {
         try {
            resp.close();
         }catch(Exception e) {
            e.printStackTrace();
         }
      }
   }

   private byte[] loadFile(File file, String path) throws Exception {
      ByteArrayOutputStream buffer = new ByteArrayOutputStream();
      InputStream input = null;
      
      if(!file.exists()) {
         file = new File(tempPath, path);
      }
      if(file.exists()) {
         input = new FileInputStream(file);
      } else {
         input = ScriptFileServer.class.getResourceAsStream(path);
      }
      byte[] chunk = new byte[1024];
      int count = 0;

      while((count = input.read(chunk)) != -1) {
         buffer.write(chunk, 0, count);
      }
      input.close();
      return buffer.toByteArray();
   }

   public class LogAgent implements TraceAnalyzer {

      @Override
      public Trace attach(SelectableChannel channel) {
         return new LogTrace(channel);
      }

      @Override
      public void stop() {}
   }

   public class LogTrace implements Trace {

      private final SelectableChannel channel;

      public LogTrace(SelectableChannel channel) {
         this.channel = channel;
      }

      @Override
      public void trace(Object event) {
         trace(event, "");
      }

      @Override
      public void trace(Object event, Object value) {         
         String message = String.format("[%s] [%s] %s", channel, event, value);

         if(value instanceof Exception) {
            Exception cause = (Exception)value;

            System.err.print(message);
            cause.printStackTrace(System.err);
            System.err.println();
         } else {
            System.err.println(message);
         }
      }
   }

   private static class LogConsole {

      private final BlockingQueue<String> events;
      private final Date time;
      private final String from;
      private final int limit;

      public LogConsole(String from, int limit) {
         this.events = new LinkedBlockingQueue<String>();
         this.time = new Date();
         this.from = from;
         this.limit = limit;
      }

      public String getCreationTime() {
         return time.toString() + "(" + from + ")";
      }

      public void update(String event) {
         int size = events.size();

         if(size > limit) {
            events.poll();
         }
         events.offer(event);
      }

      public String toString() {
         StringBuilder builder = new StringBuilder("<html>");
         builder.append("<html>");
         builder.append("<head>");
         builder.append("<meta name='viewport' content='width=device-width, initial-scale=1'>");            
         builder.append("</head>");
         builder.append("<body>");
         builder.append("<h1>");
         builder.append(from);
         builder.append(" ");
         builder.append(time);
         builder.append("</h1><pre>");
         for(String event : events) {
            builder.append(event);
            builder.append("<br>");
         }
         builder.append("</pre>");
         builder.append("</body>");
         builder.append("</html>");
         return builder.toString();
      }
   }

}
