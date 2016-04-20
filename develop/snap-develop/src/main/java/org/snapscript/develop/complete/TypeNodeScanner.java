package org.snapscript.develop.complete;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.simpleframework.http.Path;
import org.snapscript.agent.ConsoleLogger;
import org.snapscript.develop.common.FileAction;
import org.snapscript.develop.common.FileProcessor;
import org.snapscript.develop.common.FileReader;
import org.snapscript.develop.common.TypeNode;
import org.snapscript.develop.common.TypeNodeFinder;
import org.snapscript.develop.configuration.ConfigurationClassLoader;
import org.snapscript.develop.http.project.Project;
import org.snapscript.develop.http.project.ProjectBuilder;

public class TypeNodeScanner {

   private final FileProcessor<Map<String, TypeNode>> processor;
   private final FileAction<Map<String, TypeNode>> action;
   private final ProjectBuilder builder;
   private final ConsoleLogger logger;
   
   public TypeNodeScanner(ProjectBuilder builder, ConfigurationClassLoader loader, ConsoleLogger logger) {
      this(builder, loader, logger, 10);
   }
   
   public TypeNodeScanner(ProjectBuilder builder, ConfigurationClassLoader loader, ConsoleLogger logger, int threads) {
      this.action = new CompileAction(builder, loader, logger);
      this.processor = new FileProcessor<Map<String, TypeNode>>(action, threads);
      this.builder = builder;
      this.logger = logger;
   }
   
   public Map<String, String> compileProject(Path path, String prefix) throws Exception {
      Project project = builder.createProject(path);
      String name = project.getProjectName();
      File directory = project.getProjectPath();
      String root = directory.getCanonicalPath();
      long start = System.currentTimeMillis();
      int length = root.length();
      
      if(root.endsWith("/")) {
         root = root.substring(0, length -1);
      }
      Map<String, String> typeNodes = new HashMap<String, String>();
      
      try {
         Set<Map<String, TypeNode>> resourceTypes = processor.process(name, root + "/**.snap"); // build all resources
      
         for(Map<String, TypeNode> types : resourceTypes) {
            Set<String> typeNames = types.keySet();
            
            for(String typeName : typeNames) {
               TypeNode typeNode = types.get(typeName);
               String typePath = typeNode.getResource();
               
               if(typeName.startsWith(prefix)) {
                  typeNodes.put(typeName + ":" + typePath, typeName);
               }
            }
         }
         return typeNodes;
      } finally {
         long finish = System.currentTimeMillis();
         long duration = finish - start;
         
         logger.debug("Took " + duration + " ms to compile project " + name);
      }
   }
   
   private static class CompileAction implements FileAction<Map<String, TypeNode>> {
   
      private final ProjectBuilder builder;
      private final TypeNodeFinder finder;
      private final ConsoleLogger logger;
      
      public CompileAction(ProjectBuilder builder, ConfigurationClassLoader loader, ConsoleLogger logger) {
         this.finder = new TypeNodeFinder(loader, logger);
         this.builder = builder;
         this.logger = logger;
      }
      
      @Override
      public Map<String, TypeNode> execute(String reference, File file) throws Exception {
         Project project = builder.getProject(reference);
         String name = project.getProjectName();
         File root = project.getProjectPath();
         String rootPath = root.getCanonicalPath();
         String filePath = file.getCanonicalPath();
         String relativePath = filePath.replace(rootPath, "");
         String resourcePath = relativePath.replace(File.separatorChar, '/');
         
         if(!resourcePath.startsWith("/")) {
            resourcePath = "/" + resourcePath;
         }
         String source = FileReader.readText(file);
         
         logger.debug("Compiling " + resourcePath + " in project " + reference);
         
         return finder.parse(root, name, resourcePath, source);
      }
   }
}
