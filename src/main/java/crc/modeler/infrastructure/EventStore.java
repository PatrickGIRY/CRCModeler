package crc.modeler.infrastructure;

import java.util.Collection;
import java.util.stream.Stream;

public interface EventStore {
    Stream<Event> readEvents();

    void appendEvents(Collection<Event> newEvents);
}
