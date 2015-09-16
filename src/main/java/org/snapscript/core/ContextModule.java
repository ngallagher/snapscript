package org.snapscript.core;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ContextModule implements Module {

   private final List<Function> functions;    
   private final Context context;
   private final Scope scope;
   private final String name;

   public ContextModule(Context context) {
      this(context, null);
   }
   
   public ContextModule(Context context, String name) {
      this.functions = new CopyOnWriteArrayList<Function>();
      this.scope = new ModuleScope(this);
      this.context = context;
      this.name = name;
   }

   @Override
   public Context getContext() {
      return context;
   }

   @Override
   public List<Function> getFunctions() {
      return functions;
   }
   
   @Override
   public Type addType(String name) {
      try {
         TypeLoader loader = context.getLoader();
         return loader.load(name, this.name,true);
      } catch(Exception e){
         throw new IllegalStateException(e);
      }
   }

   @Override
   public Type getType(String name) {
      try {
         TypeLoader loader = context.getLoader();
         return loader.load(name, this.name,false);
      } catch(Exception e){
         throw new IllegalStateException(e);
      }
   }
   
   @Override
   public Type getType(Class type) {
      try {
         TypeLoader loader = context.getLoader();
         return loader.load(type);
      } catch(Exception e){
         throw new IllegalStateException(e);
      }
   }   

   @Override
   public Scope getScope() {
      return scope;
   }

   @Override
   public String toString() {
      return name;
   }
}
