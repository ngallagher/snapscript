package org.snapscript.develop.complete;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.Executor;

import org.snapscript.compile.Compiler;
import org.snapscript.compile.StoreContext;
import org.snapscript.compile.StringCompiler;
import org.snapscript.core.Context;
import org.snapscript.core.EmptyModel;
import org.snapscript.core.Model;
import org.snapscript.core.ScopeMerger;
import org.snapscript.core.store.FileStore;
import org.snapscript.core.store.Store;

public class Completion {

   private final Map<String, TypeNode> types;
   private final Map<String, String> tokens;
   private final List<String> lines;
   private final ScopeMerger merger;
   private final Compiler compiler;
   private final Context context;
   private final Model model;
   private final Store store;
   private final String resource;
   private final String complete;
   private final String source;
   private final String prefix;
   private final int line;
   
   public Completion(Executor executor, List<String> lines, File root, String source, String resource, String prefix, String complete, int line) {
      this.types = new HashMap<String, TypeNode>();
      this.tokens = new TreeMap<String, String>();
      this.model = new EmptyModel();
      this.store = new FileStore(root);
      this.context = new StoreContext(store, executor);
      this.compiler = new StringCompiler(context);
      this.merger = new ScopeMerger(context);
      this.resource = resource;
      this.complete = complete;
      this.source = source;
      this.prefix = prefix;
      this.lines = lines;
      this.line = line;
   }
   
   public Map<String, TypeNode> getTypes() {
      return types;
   }
   
   public Map<String, String> getTokens() {
      return tokens;
   }
   
   public ScopeMerger getMerger() {
      return merger;
   }

   public Compiler getCompiler() {
      return compiler;
   }

   public Context getContext() {
      return context;
   }
   
   public Model getModel() {
      return model;
   }

   public Store getStore() {
      return store;
   }

   public List<String> getLines() {
      return lines;
   }

   public String getResource() {
      return resource;
   }

   public String getComplete() {
      return complete;
   }

   public String getSource() {
      return source;
   }

   public String getPrefix() {
      return prefix;
   }

   public int getLine() {
      return line;
   } 
}
