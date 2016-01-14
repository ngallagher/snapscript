package org.snapscript.web.binary.event;

import java.io.IOException;

import org.snapscript.web.binary.BinaryMessage;

public interface ProcessEventMarshaller<T extends ProcessEvent> {
   T fromMessage(BinaryMessage message) throws IOException;
   BinaryMessage toMessage(T value) throws IOException;
}
