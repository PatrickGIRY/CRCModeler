package crc.modeler.infrastructure;

import java.util.stream.Stream;

public interface Command<T> {
    Stream<Event> decide(T state);
}
