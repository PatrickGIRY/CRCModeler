package crc.modeler.infrastructure;

import org.assertj.core.util.Files;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

public class FileEventStoreTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileEventStoreTest.class);

    @Test
    public void should_store_one_event_appended() throws Exception {
        File temporaryFile = Files.newTemporaryFile();
        Path path = temporaryFile.toPath();
        LOGGER.info("temporary file path {}", path);
        FileEventStore eventStore = new FileEventStore(path);
        Event event = Event.createEvent("CREATED", UUID.randomUUID());
        eventStore.appendEvents(Collections.singletonList(event));
        Stream<Event> events = eventStore.readEvents();
        assertThat(events).extracting(Event::getEventType, Event::getAggregateId)
                .containsExactly(tuple(event.getEventType(), event.getAggregateId()));
    }

    @Test
    public void should_store_events_appended() throws Exception {
        File temporaryFile = Files.newTemporaryFile();
        Path path = temporaryFile.toPath();
        LOGGER.info("temporary file path {}", path);
        FileEventStore eventStore = new FileEventStore(path);
        UUID aggregateId = UUID.randomUUID();
        Event event1 = Event.createEvent("CREATED", aggregateId);
        Event event2 = Event.createEvent("UPDATED", aggregateId);
        eventStore.appendEvents(Arrays.asList(event1, event2));
        Stream<Event> events = eventStore.readEvents();
        assertThat(events).extracting(Event::getEventType, Event::getAggregateId)
                .containsExactly(
                        tuple(event1.getEventType(), event1.getAggregateId()),
                        tuple(event2.getEventType(), event2.getAggregateId())
                );
    }

    @Test
    public void should_return_empty_stream_when_event_store_file_not_exist() throws Exception {
        FileEventStore eventStore = new FileEventStore(Paths.get("foo"));
        assertThat(eventStore.readEvents()).isEmpty();
    }
}