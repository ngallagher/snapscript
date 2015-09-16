package org.snapscript.core;

public class Function<T> {

   private final Invocation<T> invocation;
   private final Signature signature;
   private final String description;
   private final String name;

   public Function(Signature signature, Invocation<T> invocation, String description, String name){
      this.description = description;
      this.invocation = invocation;
      this.signature = signature;
      this.name = name;
   }
   
   public String getName(){
      return name;
   }
   
   public Signature getSignature(){
      return signature;
   }
   
   public Invocation<T> getInvocation(){
      return invocation;
   }

   public String getDescription() {
      return description;
   }
   
   @Override
   public String toString(){
      return description;
   }
}
/*
package com.zuooh.script.core;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

public class FunctionBinder {
   
   public static final FunctionBinder INSTANCE = new FunctionBinder();

   private final Map<SignatureKey, Function> cache;
   private final Set<SignatureKey> failures;

   public FunctionBinder() {
      this.cache = new ConcurrentHashMap<SignatureKey, Function>();
      this.failures = new CopyOnWriteArraySet<SignatureKey>();
   }
   public Function mastch(Module module, String name, Type... parameters) throws Exception {
      return mastch(module, name, Arrays.asList(parameters));
   }
   public Function mastch(Module module, String name, List<Type> parameters) throws Exception {
      SignatureKey key = getSignatureKey(name, module, parameters);
      Function result = getCacheMethod(key);

      if(result == null) {
         List<Function> list = module.getFunctions();

         for(Function method : list) {
            String methodName = method.getName();

            if(methodName.equals(name)) {
               Signature signature = method.getSignature();
               List<Type> methodParameters = signature.getTypes();
               int modifiers = signature.getModifiers();
               
               if(isMatch(parameters, methodParameters, (modifiers & 0x00000080) != 0)) {
                  cache.put(key, method);
                  return method;
               }
            }
         } 
         
      }
      return result;
   }   

   public Function mastch(Type type, String name, Type... parameters) throws Exception {
      return mastch(type, name, Arrays.asList(parameters));
   }
   public Function mastch(Type type, String name, List<Type> parameters) throws Exception {
      SignatureKey key = getSignatureKey(name, type, parameters);
      Function result = getCacheMethod(key);

      if(result == null) {
         Iterator<Type> hier=type.getTypes().iterator();
         Type base = type;

         while(base != null) {
            List<Function> list = base.getFunctions();

            for(Function method : list) {
               String methodName = method.getName();

               if(methodName.equals(name)) {
                  Signature signature = method.getSignature();
                  List<Type> methodParameters = signature.getTypes();
                  int modifiers = signature.getModifiers();
                  
                  if(isMatch(parameters, methodParameters, (modifiers & 0x00000080) != 0)) {
                     cache.put(key, method);
                     return method;
                  }
               }
            }
            if(!hier.hasNext()) { // no more supers
               break;
            }
            base = hier.next();
         }
      }
      return result;
   }

   private boolean isMatch(List<Type> argumentTypes, List<Type> methodParameters, boolean variableArguments) {
      if(methodParameters.size() == argumentTypes.size() || variableArguments) {
         int matchesRemaining = argumentTypes.size();
   
         for(int i = 0; i < methodParameters.size() && i < argumentTypes.size(); i++) {
            Type methodParamConstraint = methodParameters.get(i);
            Type argumentType = argumentTypes.get(i);
   
            if(methodParamConstraint == null || argumentType==null) {// wild
               matchesRemaining--;
            } else if(argumentType == methodParamConstraint) {
               matchesRemaining--;               
            } else if(argumentType.getTypes().contains(methodParamConstraint)) {
               matchesRemaining--;
            //} else if(isNumber(methodParamConstraint) && isNumber(argumentType)){
            //   matchesRemaining--;
            } else {
               break;
            }
         }         
         if(matchesRemaining > 0) {
            int startIndex = methodParameters.size() - 1;
            
            if(variableArguments) {
               Type methodParamConstraint = methodParameters.get(startIndex);
               
               if(methodParamConstraint.getEntry() == null) {
                  throw new IllegalStateException("Last parameter in varargs method is not an array");
               }
               Type methodParamComponentTypeConstraint = methodParamConstraint.getEntry();
               
               for(int i = startIndex; i < argumentTypes.size(); i++) {
                  Type argumentType = argumentTypes.get(i);
         
                  if(methodParamComponentTypeConstraint == null || argumentType==null) {
                     matchesRemaining--;
                  } else if(argumentType == methodParamComponentTypeConstraint) {
                     matchesRemaining--;                     
                  } else if(argumentType.getTypes().contains(methodParamComponentTypeConstraint)) {
                     matchesRemaining--;
                  //} else if(isNumber(methodParamComponentTypeConstraint) && isNumber(argumentType)){
                  //   matchesRemaining--;                    
                  } else {
                     break;
                  }
               } 
               //return matchesRemaining <= 1;
            }
         }
         return matchesRemaining == 0;
      }
      return false;
   }   
   
   private Function getCacheMethod(SignatureKey key) {
      return cache.get(key);
   }

   private SignatureKey getSignatureKey(String name, Object type, List<Type> parameters) {
      return new SignatureKey(name, type, parameters);
   }
   
   private static class SignatureKey {      

      private final List<Type> types;
      private final Object type;
      private final String name;
      private final int length;
      
      public SignatureKey(String name, Object type, List<Type> types) {
         this.length = name.length();
         this.types = types;
         this.name = name;
         this.type = type;
      }
      
      @Override
      public boolean equals(Object object) {
         if(this != object) {
            return equals((SignatureKey)object);
         }
         return true;
      }
      
      public boolean equals(SignatureKey object) {
         if(object.type != type) {
            return false;
         }
         if(object.types.size() != types.size()) {
            return false;
         }
         if(object.length != length) {
            return false;
         }
         for(int i = 0; i < types.size(); i++) {
            if(types.get(i) != object.types.get(i)) {
               return false;
            }         
         }
         return object.name.equals(name);
      }
      
      @Override
      public int hashCode() {
         return name.hashCode();
      }
      
      @Override
      public String toString() {
         return name;
      }
   }
}
*/