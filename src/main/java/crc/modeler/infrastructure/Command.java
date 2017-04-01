package crc.modeler.infrastructure;

import java.util.Collection;

public interface Command<T> {
    Collection<Event> decide(T state);
}
