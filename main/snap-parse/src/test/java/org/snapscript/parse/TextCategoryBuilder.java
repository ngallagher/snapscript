package org.snapscript.parse;

import org.snapscript.parse.TextCategory;

public class TextCategoryBuilder {
   public static void main(String[] list) throws Exception {
      for(int i=0;i<TextCategory.INDEX.length;){
         for(int j=0;j<8&&i<TextCategory.INDEX.length;j++,i++){
            if(j>0){
               System.err.print(", ");
            }
            String x=Integer.toHexString(TextCategory.INDEX[i]);
            if(x.length()==1){
               x="0x000"+x;
            }else if(x.length()==2){
               x="0x00"+x;
            }else if(x.length()==3){
               x="0x0"+x;
            }else if(x.length()==4){
               x="0x"+x;
            }
            System.err.print(x);
         }
         System.err.println(",");
      }
   }
}
