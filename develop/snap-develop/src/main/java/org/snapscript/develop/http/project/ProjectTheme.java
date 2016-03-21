package org.snapscript.develop.http.project;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;

@Root
public class ProjectTheme {
   
   @Path("font")
   @Text
   private String font;

   @Path("font")
   @Attribute
   private String size;
   
   public ProjectTheme(){
      super();
   }

   public String getFont() {
      return font;
   }

   public void setFont(String font) {
      this.font = font;
   }

   public String getSize() {
      return size;
   }

   public void setSize(String size) {
      this.size = size;
   } 
}
