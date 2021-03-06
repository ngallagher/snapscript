package org.snapscript.parse;


public class TextCategoryBuilder {
   public static void main(String[] list) throws Exception {
      for(int i=0;i<TextCategoryBuilder.TYPES.length;){
         for(int j=0;j<8&&i<TextCategoryBuilder.TYPES.length;j++,i++){
            if(j>0){
               System.err.print(", ");
            }
            String x=Integer.toHexString(TextCategoryBuilder.TYPES[i]);
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
   // generated from TableGenerator
   private static final short[] TYPES = {
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.QUOTE | TextCategory.SPECIAL,
      TextCategory.NONE,
      TextCategory.DOLLAR,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.QUOTE | TextCategory.SPECIAL,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.MINUS,
      TextCategory.PERIOD,
      TextCategory.NONE,
      TextCategory.DIGIT | TextCategory.HEXIDECIMAL | TextCategory.IDENTIFIER | TextCategory.BINARY,
      TextCategory.DIGIT | TextCategory.HEXIDECIMAL | TextCategory.IDENTIFIER | TextCategory.BINARY,
      TextCategory.DIGIT | TextCategory.HEXIDECIMAL | TextCategory.IDENTIFIER,
      TextCategory.DIGIT | TextCategory.HEXIDECIMAL | TextCategory.IDENTIFIER,
      TextCategory.DIGIT | TextCategory.HEXIDECIMAL | TextCategory.IDENTIFIER,
      TextCategory.DIGIT | TextCategory.HEXIDECIMAL | TextCategory.IDENTIFIER,
      TextCategory.DIGIT | TextCategory.HEXIDECIMAL | TextCategory.IDENTIFIER,
      TextCategory.DIGIT | TextCategory.HEXIDECIMAL | TextCategory.IDENTIFIER,
      TextCategory.DIGIT | TextCategory.HEXIDECIMAL | TextCategory.IDENTIFIER,
      TextCategory.DIGIT | TextCategory.HEXIDECIMAL | TextCategory.IDENTIFIER,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.LETTER | TextCategory.IDENTIFIER | TextCategory.HEXIDECIMAL | TextCategory.CAPITAL,
      TextCategory.LETTER | TextCategory.IDENTIFIER | TextCategory.HEXIDECIMAL | TextCategory.CAPITAL,
      TextCategory.LETTER | TextCategory.IDENTIFIER | TextCategory.HEXIDECIMAL | TextCategory.CAPITAL,
      TextCategory.LETTER | TextCategory.IDENTIFIER | TextCategory.HEXIDECIMAL | TextCategory.DOUBLE | TextCategory.CAPITAL,
      TextCategory.LETTER | TextCategory.IDENTIFIER | TextCategory.HEXIDECIMAL | TextCategory.CAPITAL,
      TextCategory.LETTER | TextCategory.IDENTIFIER | TextCategory.HEXIDECIMAL | TextCategory.FLOAT | TextCategory.CAPITAL,
      TextCategory.LETTER | TextCategory.IDENTIFIER | TextCategory.CAPITAL,
      TextCategory.LETTER | TextCategory.IDENTIFIER | TextCategory.CAPITAL,
      TextCategory.LETTER | TextCategory.IDENTIFIER | TextCategory.CAPITAL,
      TextCategory.LETTER | TextCategory.IDENTIFIER | TextCategory.CAPITAL,
      TextCategory.LETTER | TextCategory.IDENTIFIER | TextCategory.CAPITAL,
      TextCategory.LETTER | TextCategory.IDENTIFIER | TextCategory.LONG | TextCategory.CAPITAL,
      TextCategory.LETTER | TextCategory.IDENTIFIER | TextCategory.CAPITAL,
      TextCategory.LETTER | TextCategory.IDENTIFIER | TextCategory.CAPITAL,
      TextCategory.LETTER | TextCategory.IDENTIFIER | TextCategory.CAPITAL,
      TextCategory.LETTER | TextCategory.IDENTIFIER | TextCategory.CAPITAL,
      TextCategory.LETTER | TextCategory.IDENTIFIER | TextCategory.CAPITAL,
      TextCategory.LETTER | TextCategory.IDENTIFIER | TextCategory.CAPITAL,
      TextCategory.LETTER | TextCategory.IDENTIFIER | TextCategory.CAPITAL,
      TextCategory.LETTER | TextCategory.IDENTIFIER | TextCategory.CAPITAL,
      TextCategory.LETTER | TextCategory.IDENTIFIER | TextCategory.CAPITAL | TextCategory.SPECIAL,
      TextCategory.LETTER | TextCategory.IDENTIFIER | TextCategory.CAPITAL,
      TextCategory.LETTER | TextCategory.IDENTIFIER | TextCategory.CAPITAL,
      TextCategory.LETTER | TextCategory.IDENTIFIER | TextCategory.CAPITAL,
      TextCategory.LETTER | TextCategory.IDENTIFIER | TextCategory.CAPITAL,
      TextCategory.LETTER | TextCategory.IDENTIFIER | TextCategory.CAPITAL,
      TextCategory.NONE,
      TextCategory.SPECIAL | TextCategory.ESCAPE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.IDENTIFIER,
      TextCategory.NONE,
      TextCategory.LETTER | TextCategory.IDENTIFIER | TextCategory.HEXIDECIMAL,
      TextCategory.LETTER | TextCategory.IDENTIFIER | TextCategory.HEXIDECIMAL | TextCategory.SPECIAL,
      TextCategory.LETTER | TextCategory.IDENTIFIER | TextCategory.HEXIDECIMAL,
      TextCategory.LETTER | TextCategory.IDENTIFIER | TextCategory.HEXIDECIMAL | TextCategory.DOUBLE,
      TextCategory.LETTER | TextCategory.IDENTIFIER | TextCategory.HEXIDECIMAL,
      TextCategory.LETTER | TextCategory.IDENTIFIER | TextCategory.HEXIDECIMAL | TextCategory.FLOAT | TextCategory.SPECIAL,
      TextCategory.LETTER | TextCategory.IDENTIFIER,
      TextCategory.LETTER | TextCategory.IDENTIFIER,
      TextCategory.LETTER | TextCategory.IDENTIFIER,
      TextCategory.LETTER | TextCategory.IDENTIFIER,
      TextCategory.LETTER | TextCategory.IDENTIFIER,
      TextCategory.LETTER | TextCategory.IDENTIFIER | TextCategory.LONG,
      TextCategory.LETTER | TextCategory.IDENTIFIER,
      TextCategory.LETTER | TextCategory.IDENTIFIER | TextCategory.SPECIAL,
      TextCategory.LETTER | TextCategory.IDENTIFIER,
      TextCategory.LETTER | TextCategory.IDENTIFIER,
      TextCategory.LETTER | TextCategory.IDENTIFIER,
      TextCategory.LETTER | TextCategory.IDENTIFIER | TextCategory.SPECIAL,
      TextCategory.LETTER | TextCategory.IDENTIFIER,
      TextCategory.LETTER | TextCategory.IDENTIFIER | TextCategory.SPECIAL,
      TextCategory.LETTER | TextCategory.IDENTIFIER | TextCategory.SPECIAL,
      TextCategory.LETTER | TextCategory.IDENTIFIER,
      TextCategory.LETTER | TextCategory.IDENTIFIER,
      TextCategory.LETTER | TextCategory.IDENTIFIER,
      TextCategory.LETTER | TextCategory.IDENTIFIER,
      TextCategory.LETTER | TextCategory.IDENTIFIER,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      TextCategory.NONE,
      };

}