package org.snapscript.engine.http.project;

public class ProjectMode {

   private static final String SINGLE_MODE = "debug";
   private static final String MULTIPLE_MODE = "multiple";
   
   private final String mode;
   
   public ProjectMode(String mode) {
      this.mode = mode;
   }
   
   public boolean isSingleMode() {
      return mode.equals(SINGLE_MODE);
   }
   
   public boolean isMultipleMode() {
      return mode.equals(MULTIPLE_MODE);
   }
   
   public String getMode() {
      return mode;
   }
}
