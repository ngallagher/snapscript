package org.snapscript.web.resource.tree;

import java.io.File;
import java.io.PrintStream;

import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.snapscript.web.resource.Resource;

public class TreeResource implements Resource {
   
   private final File resourcePath;
   
   public TreeResource(File resourcePath) {
      this.resourcePath = resourcePath;
   }

   @Override
   public void handle(Request request, Response response) throws Throwable {
      StringBuilder builder = new StringBuilder();
      builder.append("<div id=\"tree\">\n");
      builder.append("<ul id=\"treeData\" style=\"display: none;\">\n");
      buildTree(builder, resourcePath, "", "  ", "id", 1);
      builder.append("</ul>\n");
      builder.append("</div>\n");
      String tree = builder.toString();
      System.err.println(tree);
      PrintStream out = response.getPrintStream();
      response.setContentType("text/html");
      out.println(tree);
      out.close();
   }
   
   private void buildTree(StringBuilder builder, File file, String path, String pad, String prefix, int id) throws Exception {
      String name = file.getName();
      if(file.isDirectory()) {
         builder.append(pad);
         builder.append("<li id=\"");
         builder.append(prefix);
         builder.append(id);
         builder.append("\" class=\"folder\">");
         builder.append(name);
         builder.append("\n");
         
         File[] list = file.listFiles();
         if(list != null && list.length > 0) {
            prefix = prefix + id + ".";
            builder.append(pad);
            builder.append("<ul>\n");
            for(int i = 0; i < list.length; i++) {
               File entry = list[i];
               String title = entry.getName();
               
               buildTree(builder, entry, path + "/" + title, pad + "  ", prefix, i + 1);
            }
            builder.append(pad);
            builder.append("</ul>\n");
         }
      } else {
         builder.append(pad);
         builder.append("<li id=\"");
         builder.append(prefix);
         builder.append(id);
         builder.append("\" title=\"");
         builder.append(path);
         builder.append("\">");
         builder.append(name);
         builder.append("\n");
      }
   }

}
