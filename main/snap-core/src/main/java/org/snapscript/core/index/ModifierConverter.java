package org.snapscript.core.index;

import static org.snapscript.core.ModifierType.ABSTRACT;
import static org.snapscript.core.ModifierType.CONSTANT;
import static org.snapscript.core.ModifierType.PRIVATE;
import static org.snapscript.core.ModifierType.PUBLIC;
import static org.snapscript.core.ModifierType.STATIC;
import static org.snapscript.core.ModifierType.VARARGS;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class ModifierConverter {
   
   public int convert(Method method) {
      int result = 0;
      
      if(method != null) {
         int modifiers = method.getModifiers();
   
         if(method.isVarArgs()) {
            result |= VARARGS.mask;
         }
         if(Modifier.isAbstract(modifiers)) {
            result |= ABSTRACT.mask;
         }
         if(Modifier.isFinal(modifiers)) {
            result |= CONSTANT.mask;
         }
         if(Modifier.isPrivate(modifiers)) {
            result |= PRIVATE.mask;
         }
         if(Modifier.isPublic(modifiers)) {
            result |= PUBLIC.mask;
         }
         if(Modifier.isStatic(modifiers)) {
            result |= STATIC.mask;
         }
      }
      return result;
   }
   
   public int convert(Constructor constructor) {
      int result = 0;
      
      if(constructor != null) {
         int modifiers = constructor.getModifiers();
   
         if(constructor.isVarArgs()) {
            result |= VARARGS.mask;
         }
         if(Modifier.isAbstract(modifiers)) {
            result |= ABSTRACT.mask;
         }
         if(Modifier.isFinal(modifiers)) {
            result |= CONSTANT.mask;
         }
         if(Modifier.isPrivate(modifiers)) {
            result |= PRIVATE.mask;
         }
         if(Modifier.isPublic(modifiers)) {
            result |= PUBLIC.mask;
         }
         result |= STATIC.mask;
      }
      return result;
   }
   
   public int convert(Field field) {
      int result = 0;
      
      if(field != null) {
         int modifiers = field.getModifiers();
         
         if(Modifier.isFinal(modifiers)) {
            result |= CONSTANT.mask;
         }
         if(Modifier.isPrivate(modifiers)) {
            result |= PRIVATE.mask;
         }
         if(Modifier.isPublic(modifiers)) {
            result |= PUBLIC.mask;
         }
         if(Modifier.isStatic(modifiers)) {
            result |= STATIC.mask;
         }
      }
      return result;
   }
}
