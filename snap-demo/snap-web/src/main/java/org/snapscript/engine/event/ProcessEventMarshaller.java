package org.snapscript.engine.event;

import java.io.IOException;

import org.snapscript.engine.message.BinaryMessage;

public interface ProcessEventMarshaller<T extends ProcessEvent> {
   T fromMessage(BinaryMessage message) throws IOException;
   BinaryMessage toMessage(T value) throws IOException;
}
