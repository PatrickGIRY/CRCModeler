package crc.modeler.infrastructure;

import java.util.stream.Stream;

public interface EventStore {
    Stream<Event> readEvents();

    void appendEvents(Stream<Event> nextEvents);
}
