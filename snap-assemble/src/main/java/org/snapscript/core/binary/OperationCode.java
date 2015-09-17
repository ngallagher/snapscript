package org.snapscript.core.binary;

public enum OperationCode {
   VERSION('V'),
   BEGIN('G'),
   END('E'),
   PUSH('P'),
   POP('O'),
   NONE('N'),
   INTEGER('I'), 
   TEXT('T'), 
   DOUBLE('D'), 
   FLOAT('F'), 
   LONG('L'), 
   SHORT('S'), 
   OCTET('O'), 
   BOOLEAN('B'), 
   CHARACTER('C');

   public final char code;

   private OperationCode(char code) {
      this.code = code;
   }   

   public static OperationCode resolveCode(char code) {
      if (code == 'G') {
         return BEGIN;
      }
      if (code == 'E') {
         return END;
      }
      if (code == 'V') {
         return VERSION;
      }          
      if (code == 'P') {
         return PUSH;
      }
      if (code == 'O') {
         return POP;
      }
      if (code == 'N') {
         return NONE;
      }      
      if (code == 'I') {
         return INTEGER;
      }
      if (code == 'T') {
         return TEXT;
      }
      if (code == 'D') {
         return DOUBLE;
      }
      if (code == 'F') {
         return FLOAT;
      }
      if (code == 'L') {
         return LONG;
      }
      if (code == 'S') {
         return SHORT;
      }
      if (code == 'O') {
         return OCTET;
      }
      if (code == 'B') {
         return BOOLEAN;
      }
      if (code == 'C') {
         return CHARACTER;
      }    
      throw new IllegalArgumentException("No match for " + code);
   }
   
   public static OperationCode resolveCode(Object value) {
      Class type = value.getClass();
      
      if (type == int.class) {
         return INTEGER;
      }
      if (type == double.class) {
         return DOUBLE;
      }
      if (type == float.class) {
         return FLOAT;
      }
      if (type == boolean.class) {
         return BOOLEAN;
      }
      if (type == byte.class) {
         return OCTET;
      }
      if (type == short.class) {
         return SHORT;
      }
      if (type == long.class) {
         return LONG;
      }
      if (type == char.class) {
         return CHARACTER;
      }
      if (type == String.class) {
         return TEXT;
      }
      if (type == Integer.class) {
         return INTEGER;
      }
      if (type == Double.class) {
         return DOUBLE;
      }
      if (type == Float.class) {
         return FLOAT;
      }
      if (type == Boolean.class) {
         return BOOLEAN;
      }
      if (type == Byte.class) {
         return OCTET;
      }
      if (type == Short.class) {
         return SHORT;
      }
      if (type == Long.class) {
         return LONG;
      }
      if (type == Character.class) {
         return CHARACTER;
      }
      throw new IllegalArgumentException("No match for " + type);
   }
}
