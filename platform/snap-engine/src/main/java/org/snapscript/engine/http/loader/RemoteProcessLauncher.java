package org.snapscript.engine.http.loader;

import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;

public class RemoteProcessLauncher {

   public static void main(String[] list) throws Exception {
      URI classes = URI.create(list[0]);
      String type = list[1];
      String[] arguments = Arrays.copyOfRange(list, 2, list.length);
      
      start(classes, type, arguments);
   }
   
   public static void start(URI classes, String name, String[] arguments) throws Exception {
      URL[] path = new URL[]{classes.toURL()};
      URLClassLoader loader = new URLClassLoader(path);
      Class type = loader.loadClass(name);
      Method method = type.getDeclaredMethod("main", String[].class);

      method.invoke(null, (Object)arguments);
   }
}
