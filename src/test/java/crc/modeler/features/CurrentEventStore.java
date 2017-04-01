package crc.modeler.features;

import crc.modeler.infrastructure.Event;
import crc.modeler.infrastructure.EventStore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

public class CurrentEventStore implements EventStore {
    private List<Event> storedEvents = new ArrayList<>();

    @Override
    public Stream<Event> readEvents() {
        return storedEvents.stream();
    }

    @Override
    public void appendEvents(Collection<Event> newEvents) {
        storedEvents.addAll(newEvents);
    }
}
