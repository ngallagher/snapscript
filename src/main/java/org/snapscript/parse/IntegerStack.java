package org.snapscript.parse;

import java.util.Arrays;

public class IntegerStack {

   private int[] stack;
   private int count;

   public IntegerStack() {
      this(1024);
   }
   
   public IntegerStack(int capacity) {
      this.stack = new int[capacity];
   }      

   public boolean isEmpty() {
      return count == 0;
   }
   
   public int index(int value) {
      for(int i = 0; i < count; i++) {
         int next = stack[i];
         
         if(next == value) {
            return i;
         }
      }
      return -1;     
   }
   
   public int depth(int value) {
      for(int i = count - 1; i >= 0; i--) {
         int next = stack[i];
         
         if(next == value) {
            return count - (i + 1);
         }
      }
      return -1;  
   }   

   public boolean contains(int value) {
      for(int i = 0; i < count; i++) {
         int next = stack[i];
         
         if(next == value) {
            return true;
         }
      }
      return false; 
   }   

   public void push(int value) {
      int capacity = stack.length;
      
      if(count >= capacity) {
         stack = Arrays.copyOf(stack, capacity * 2);
      }
      stack[count++] = value;
   }

   public int pop() {
      if(count > 0) {
         return stack[count-- -1];
      }
      return -1;
   }

   public int peek() {
      if(count > 0) {
         return stack[count -1];
      }
      return -1;
   }
   
   public int size() {
      return count;
   }

   public void clear() {
      count = 0;
   }
}