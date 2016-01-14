package org.snapscript.engine.message;

import java.io.DataInputStream;
import java.io.InputStream;

public class BinaryMessageConsumer {

   public final DataInputStream stream;
   
   public BinaryMessageConsumer(InputStream stream) {
      this.stream = new DataInputStream(stream);
   }
   
   public synchronized BinaryMessage consume() throws Exception {
      String agent = stream.readUTF();
      int type = stream.readInt();
      int length = stream.readInt();
      byte[] array = new byte[length];
      
      stream.readFully(array);
      
      return new BinaryMessage(agent, type, array, 0, length);
   }
}
