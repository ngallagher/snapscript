package org.snapscript.web.message;

public enum MessageType {
   PRINT_ERROR('C'),
   PRINT_OUTPUT('C'),
   PING('P'),
   PONG('P'),
   PROJECT_NAME('N'),
   PROCESS_ID('I'),
   REGISTER('R'),
   SCRIPT('S'),
   EXCEPTION('E'),
   SYNTAX_ERROR('Y'),
   COMPILE_TIME('T'),
   EXECUTE_TIME('T'),
   EXIT('X');
   
   public final char prefix;
   
   private MessageType(char prefix){
      this.prefix = prefix;
   }
}
