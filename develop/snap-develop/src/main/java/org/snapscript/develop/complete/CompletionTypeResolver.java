package org.snapscript.develop.complete;

import static org.snapscript.compile.instruction.Instruction.SCRIPT;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.CharArrayReader;
import java.io.CharArrayWriter;
import java.io.Closeable;
import java.io.Console;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilePermission;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.FilterInputStream;
import java.io.FilterOutputStream;
import java.io.FilterReader;
import java.io.FilterWriter;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.PushbackInputStream;
import java.io.PushbackReader;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.io.SequenceInputStream;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Currency;
import java.util.Date;
import java.util.Deque;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Enumeration;
import java.util.Formattable;
import java.util.Formatter;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.Observable;
import java.util.Observer;
import java.util.PriorityQueue;
import java.util.Properties;
import java.util.Queue;
import java.util.Random;
import java.util.RandomAccess;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.SimpleTimeZone;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.Stack;
import java.util.StringTokenizer;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.UUID;
import java.util.Vector;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.snapscript.agent.ConsoleLogger;
import org.snapscript.compile.Compiler;
import org.snapscript.compile.Executable;
import org.snapscript.compile.StoreContext;
import org.snapscript.compile.StringCompiler;
import org.snapscript.core.Context;
import org.snapscript.core.EmptyModel;
import org.snapscript.core.Function;
import org.snapscript.core.Model;
import org.snapscript.core.Module;
import org.snapscript.core.ModuleRegistry;
import org.snapscript.core.Package;
import org.snapscript.core.PackageLinker;
import org.snapscript.core.PathConverter;
import org.snapscript.core.PrimitivePromoter;
import org.snapscript.core.Property;
import org.snapscript.core.Scope;
import org.snapscript.core.ScopeMerger;
import org.snapscript.core.Signature;
import org.snapscript.core.Statement;
import org.snapscript.core.Type;
import org.snapscript.core.TypeLoader;
import org.snapscript.core.store.FileStore;
import org.snapscript.core.store.Store;

public class CompletionTypeResolver {
   
   private static final Class[] DEFAULT_TYPES = new Class[] {
      String.class,
      System.class,
      Runtime.class,
      Thread.class,
      Runnable.class,
      Integer.class,
      Boolean.class,
      Number.class,
      Double.class,
      Float.class,
      Long.class,
      Byte.class,
      Short.class,
      Comparable.class,
      Math.class,
      Enum.class,
      Object.class,
      StringBuilder.class,
      Character.class,
      ThreadLocal.class,
      Process.class,
      ProcessBuilder.class,
      ClassLoader.class,
      IllegalStateException.class,
      IllegalArgumentException.class,
      Exception.class,
      Map.class,
      List.class,
      ConcurrentHashMap.class,
      ArrayDeque.class,
      ArrayList.class,
      Arrays.class,
      BitSet.class,
      Calendar.class,
      Collections.class,
      Currency.class,
      Date.class,
      EnumMap.class,
      EnumSet.class,
      Formatter.class,
      GregorianCalendar.class,
      HashMap.class,
      HashSet.class,
      Hashtable.class,
      IdentityHashMap.class,
      LinkedHashMap.class,
      LinkedHashSet.class,
      LinkedList.class,
      Locale.class,
      Observable.class,
      PriorityQueue.class,
      Properties.class,
      Random.class,
      ResourceBundle.class,
      Scanner.class,
      ServiceLoader.class,
      SimpleTimeZone.class,
      Stack.class,
      StringTokenizer.class,
      Timer.class,
      TimerTask.class,
      TimeZone.class,
      TreeMap.class,
      TreeSet.class,
      UUID.class,
      Vector.class,
      WeakHashMap.class,
      Collection.class,
      Comparator.class,
      Deque.class,
      Enumeration.class,
      Formattable.class,
      Iterator.class,
      List.class,
      ListIterator.class,
      Map.class,
      Map.Entry.class,
      NavigableMap.class,
      NavigableSet.class,
      Observer.class,
      Queue.class,
      RandomAccess.class,
      Set.class,
      SortedMap.class,
      SortedSet.class,
      IOException.class,
      BufferedInputStream.class,
      BufferedOutputStream.class,
      BufferedReader.class,
      BufferedWriter.class,
      ByteArrayInputStream.class,
      ByteArrayOutputStream.class,
      CharArrayReader.class,
      CharArrayWriter.class,
      Console.class,
      DataInputStream.class,
      DataOutputStream.class,
      File.class,
      FileDescriptor.class,
      FileInputStream.class,
      FileOutputStream.class,
      FilePermission.class,
      FileReader.class,
      FileWriter.class,
      FilterInputStream.class,
      FilterOutputStream.class,
      FilterReader.class,
      FilterWriter.class,
      InputStream.class,
      InputStreamReader.class,
      LineNumberReader.class,
      ObjectInputStream.class,
      ObjectOutputStream.class,
      OutputStream.class,
      OutputStreamWriter.class,
      PipedInputStream.class,
      PipedOutputStream.class,
      PipedReader.class,
      PipedWriter.class,
      PrintStream.class,
      PrintWriter.class,
      PushbackInputStream.class,
      PushbackReader.class,
      RandomAccessFile.class,
      Reader.class,
      SequenceInputStream.class,
      StreamTokenizer.class,
      StringReader.class,
      StringWriter.class,
      Writer.class,
      Closeable.class,
      FileFilter.class,
      FilenameFilter.class,
      FileNotFoundException.class,
      Flushable.class,
      EOFException.class
   };

   private final Map<String, CompletionType> cache;
   private final PrimitivePromoter promoter;
   private final PathConverter converter;
   private final ConsoleLogger logger;
   
   public CompletionTypeResolver(ConsoleLogger logger) {
      this.cache = new ConcurrentHashMap<String, CompletionType>();
      this.promoter = new PrimitivePromoter();
      this.converter = new PathConverter();
      this.logger = logger;
   }
   
   public Map<String, CompletionType> resolveTypes(File root, String text, String resource, String prefix, String complete) {
      Model model = new EmptyModel();
      Store store = new FileStore(root);
      Context context = new StoreContext(store);
      Compiler compiler = new StringCompiler(context);
      ScopeMerger merger = new ScopeMerger(context);
      Map<String, CompletionType> types = importTypes(context, resource, prefix);
      ModuleRegistry registry = context.getRegistry();
      List<Module> modules = registry.getModules();
      
      try {
         String source = parseSource(context, text, resource, complete);
         String module = converter.createModule(resource);
         PackageLinker linker = context.getLinker();
         Package library = linker.link(module, source, SCRIPT.name);
         Scope scope = merger.merge(model, module);
         
         library.compile(scope);
      } catch(Exception e) {
         logger.log("Error compiling " + resource + ", parsing imports only for " + complete);
         
         try {
            String source = parseImportsOnly(context, text, resource);
            Executable executable = compiler.compile(source);
            executable.execute();
         }catch(Exception fatal) {
            logger.log("Error compiling imports for " + resource, fatal);
         }
      }
      for(Module imported : modules) {
        List<Type> accessible = imported.getTypes();
        String module = imported.getName();
        
        for(Type type : accessible) {
           String name = type.getName();

           if(name != null) {
              CompletionType value = new CompletionType(type, name);
              types.put(name, value);
           }
        }
        if(module != null){
           CompletionType value = new CompletionType(imported, module);
           types.put(module, value);
        }
      }
      return expandFunctions(types, prefix);
   }
   
   private Map<String, CompletionType> expandFunctions(Map<String, CompletionType> types, String prefix) {
      Set<String> names = new HashSet<String>(types.keySet());
      
      for(String name : names) {
         CompletionType type = types.get(name);
         List<Function> functions = type.getFunctions();
         List<Property> properties = type.getProperties();
         
         for(Function function : functions) {
            String key = function.getName();
            Type constraint = function.getConstraint();
           
            if(constraint != null) {
               String returns = constraint.getName();
               Signature signature = function.getSignature();
               List<String> parameters = signature.getNames();
               CompletionType match = types.get(returns);
               int count = parameters.size();
               
               if(match == null) {
                  Class real = constraint.getType();
                  
                  if(real != null) { 
                     real = promoter.promote(real);
                  }
                  if(real != null) {
                     String identifier = real.getSimpleName();
                     match = types.get(identifier);
                  }
                  if(match == null) {
                     match = new CompletionType(constraint, returns);
                     types.put(returns, match);
                  }
               }
               if(match != null) {
                  types.put(key+"(" + count +")", match);
               }
            }
         }
         for(Property property : properties) {
            String key = property.getName();
            Type constraint = property.getConstraint();
            
            if(constraint != null) {
               String declaration = constraint.getName();
               CompletionType match = types.get(declaration);
            
               if(match == null) {
                  Class real = constraint.getType();
                  
                  if(real != null) { 
                     real = promoter.promote(real);
                  }
                  if(real != null) {
                     String identifier = real.getSimpleName();
                     match = types.get(identifier);
                  }
                  if(match == null) {
                     match = new CompletionType(constraint, declaration);
                     types.put(declaration, match);
                  }
               }
               if(match != null) {
                  types.put(key, match);
               }
            }
         }
      }
      return types;
   }
   
   private Map<String, CompletionType> importTypes(Context context, String resource, String prefix) {
      Map<String, CompletionType> types = new HashMap<String, CompletionType>();
      Set<String> names = new HashSet<String>();
      
      for(Class real : DEFAULT_TYPES) {
         String name = real.getSimpleName();
         names.add(name);
      }
      try {
         TypeLoader loader = context.getLoader();
         int require = names.size();
         int actual = cache.size();
         
         if(actual < require) {
            for(Class real : DEFAULT_TYPES) {
               Type type = loader.loadType(real);
               String name = type.getName();
               
               if(name != null) {
                  CompletionType value = new CompletionType(type, name);
                  cache.put(name, value);
               }
            }
         }
         types.putAll(cache);   
      } catch(Exception e) {
         logger.log("Error compiling " + resource, e);
      }
      return types;
   }
   
   private String parseSource(Context context, String text, String resource, String complete) {
      StringBuilder builder = new StringBuilder();
      String lines[] = text.split("\\r?\\n");

      for(String line : lines) {
         if(!line.contains(complete)) {
            builder.append(line);
            builder.append("\n");
         }
      }
      return builder.toString();
   }
   
   private String parseImportsOnly(Context context, String text, String resource) {
      List<String> imports = new ArrayList<String>();
      StringBuilder builder = new StringBuilder();
      String lines[] = text.split("\\r?\\n");
      Pattern pattern = Pattern.compile("^import (.*);.*");

      for(String line : lines) {
         String token = line.trim();
        
         if(token.startsWith("import ")) {
            Matcher matcher = pattern.matcher(token);
            
            if(matcher.matches()) {
               String module = matcher.group(1);
               
               imports.add(module);
               builder.append(token);
               builder.append("\n");
            }
         }
      }
      return builder.toString();
   }
}
