package org.snapscript.develop.complete;

import java.util.concurrent.atomic.AtomicReference;

import org.snapscript.common.ArrayStack;
import org.snapscript.common.Stack;

public class CompletionBraceCounter {
   
   public static final String OPEN_BRACE = "{";
   public static final String CLOSE_BRACE = "}";

   private final AtomicReference<String> current;
   private final Stack<String> stack;

   public CompletionBraceCounter(){
      this.current = new AtomicReference<String>();
      this.stack = new ArrayStack<String>();
   }
   
   public String getType(){
      if(!stack.isEmpty()) {
         return stack.peek();
      }
      return null;
   }
   
   public void setType(String type){
      current.set(type);
   }
    
   public void setBrace(String brace) {
      String type = current.get();
      
      if(brace.equals(OPEN_BRACE)) {
         stack.push(type);
      }
      if(brace.equals(CLOSE_BRACE)){
         stack.pop();
      }
   }
}
