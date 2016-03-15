package org.snapscript.engine;

import java.util.HashMap;
import java.util.Map;

public class ProcessConfiguration {

   private Map<String, String> variables;
   private String classPath;
   private String address;
   private int maxMemory;
   private int minMemory;
   
   public ProcessConfiguration() {
      this.variables = new HashMap<String, String>();
   }
   
   public Map<String, String> getVariables() {
      return variables;
   }
   
   public void setVariables(Map<String, String> variables) {
      this.variables = variables;
   }

   public String getClassPath() {
      return classPath;
   }

   public void setClassPath(String classPath) {
      this.classPath = classPath;
   }
   
   public String getAddress() {
      return address;
   }

   public void setAddress(String address) {
      this.address = address;
   }

   public int getMaxMemory() {
      return maxMemory;
   }

   public void setMaxMemory(int maxMemory) {
      this.maxMemory = maxMemory;
   }

   public int getMinMemory() {
      return minMemory;
   }

   public void setMinMemory(int minMemory) {
      this.minMemory = minMemory;
   }
 
}
