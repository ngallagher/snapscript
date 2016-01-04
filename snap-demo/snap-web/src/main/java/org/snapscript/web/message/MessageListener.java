package org.snapscript.web.message;

public interface MessageListener {
   void onMessage(Message message);
   void onError(Exception cause);
   void onClose();
}
