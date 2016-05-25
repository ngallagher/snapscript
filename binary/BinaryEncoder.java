package com.zuooh.script.core.binary;

import static com.zuooh.script.core.binary.OperationCode.BOOLEAN;
import static com.zuooh.script.core.binary.OperationCode.CHARACTER;
import static com.zuooh.script.core.binary.OperationCode.DOUBLE;
import static com.zuooh.script.core.binary.OperationCode.FLOAT;
import static com.zuooh.script.core.binary.OperationCode.INTEGER;
import static com.zuooh.script.core.binary.OperationCode.LONG;
import static com.zuooh.script.core.binary.OperationCode.OCTET;
import static com.zuooh.script.core.binary.OperationCode.SHORT;
import static com.zuooh.script.core.binary.OperationCode.TEXT;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BinaryEncoder {
   
   private final Map<String, Integer> references;
   private final List<String> indexes;

   public BinaryEncoder() {
      this.references = new HashMap<String, Integer>();
      this.indexes = new ArrayList<String>();
   }

   public Object read(DataInput reader) throws IOException {
      if (reader.readBoolean()) {
         char code = reader.readChar();
         OperationCode format = OperationCode.resolveCode(code);

         if (format == INTEGER) {
            return reader.readInt();
         } else if (format == LONG) {
            return reader.readLong();
         } else if (format == FLOAT) {
            return reader.readFloat();
         } else if (format == DOUBLE) {
            return reader.readDouble();
         } else if (format == OCTET) {
            return reader.readByte();
         } else if (format == SHORT) {
            return reader.readShort();
         } else if (format == BOOLEAN) {
            return reader.readBoolean();
         } else if (format == CHARACTER) {
            return reader.readChar();            
         } else if (format == TEXT) {
            int index = reader.readInt();
            int size = indexes.size();
            
            if(index >= size) {
               String value = reader.readUTF();
               
               if(value != null) {
                  references.put(value, size);               
                  indexes.add(value);
               }
               return value;
            }
            return indexes.get(index);
         }
      }
      return null;
   }

   public void write(DataOutput writer, Object value) throws IOException {
      if (value != null) {
         OperationCode format = OperationCode.resolveCode(value);

         writer.writeBoolean(true);
         writer.writeChar(format.code);

         if (format == INTEGER) {
            writer.writeInt((Integer) value);
         } else if (format == LONG) {
            writer.writeLong((Long) value);
         } else if (format == FLOAT) {
            writer.writeFloat((Float) value);
         } else if (format == DOUBLE) {
            writer.writeDouble((Double) value);
         } else if (format == OCTET) {
            writer.writeByte((Byte) value);
         } else if (format == SHORT) {
            writer.writeShort((Short) value);
         } else if (format == BOOLEAN) {
            writer.writeBoolean((Boolean) value);
         } else if (format == CHARACTER) {
            writer.writeChar((Character) value);              
         } else if (format == TEXT) {
            String text = (String) value;
            Integer index = references.get(text);
            
            if(index == null) {
               int size = indexes.size();
               
               writer.writeInt(size);
               writer.writeUTF(text);
               references.put(text, size);
               indexes.add(text);
            } else {               
               writer.writeInt(index);
            }
         }
      } else {
         writer.writeBoolean(false);
      }
   }
   
   public void reset() {
      references.clear();
      indexes.clear();
   }
}

