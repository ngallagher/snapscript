package org.snapscript.develop.http.project;

import java.io.File;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.simpleframework.http.Path;
import org.snapscript.agent.ConsoleLogger;
import org.snapscript.common.ThreadPool;
import org.snapscript.develop.common.FilePatternScanner;
import org.snapscript.develop.common.FileReader;
import org.snapscript.develop.common.Problem;
import org.snapscript.develop.common.ProblemFinder;

public class ProjectCompiler {

   private final ProblemFinder compiler;
   private final ProjectBuilder builder;
   private final ConsoleLogger logger;
   private final ThreadPool pool;
   
   public ProjectCompiler(ProjectBuilder builder, ConsoleLogger logger) {
      this(builder, logger, 10);
   }
   
   public ProjectCompiler(ProjectBuilder builder, ConsoleLogger logger, int threads) {
      this.compiler = new ProblemFinder();
      this.pool = new ThreadPool(threads);
      this.builder = builder;
      this.logger = logger;
   }
   
   public Set<Problem> build(Path path) throws Exception {
      Project project = builder.createProject(path);
      File directory = project.getProjectPath();
      String root = directory.getCanonicalPath();
      int length = root.length();
      
      if(root.endsWith("/")) {
         root = root.substring(0, length -1);
      }
      return build(project, root + "/**.snap"); // build all resources
   }
   
   private Set<Problem> build(Project project, String pattern) throws Exception {
      List<File> resources = FilePatternScanner.scan(pattern);
      int count = resources.size();
      long start = System.currentTimeMillis();
      
      logger.debug("Scan of " + pattern + " found " + count + " resources");
      
      try {
         Set<Problem> problems = new CopyOnWriteArraySet<Problem>();
         CountDownLatch latch = new CountDownLatch(count);
         
         for(File resource : resources) {
            CompileTask task = new CompileTask(problems, latch, project, resource);
            pool.execute(task);
         }
         latch.await(5000, TimeUnit.MILLISECONDS);
         return problems;
      } finally {
         long finish = System.currentTimeMillis();
         long duration = finish - start;
         
         logger.log("Took " + duration + " ms to compile " + count + " resources");
      }
   }
   
   private class CompileTask implements Runnable {
   
      private final Set<Problem> problems;
      private final CountDownLatch latch;
      private final Project project;
      private final File file;
      
      public CompileTask(Set<Problem> problems, CountDownLatch latch, Project project, File file) {
         this.problems = problems;
         this.project = project;
         this.latch = latch;
         this.file = file;
      }
      
      public void run() {
         try {
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
            Problem problem = compiler.parse(name, resourcePath, source);

            if(problem != null) {
               problems.add(problem);
            }
         } catch(Exception e) {
            logger.log("Could not parse file " + file, e);
         } finally {
            latch.countDown();
         }
      }
   }
}
