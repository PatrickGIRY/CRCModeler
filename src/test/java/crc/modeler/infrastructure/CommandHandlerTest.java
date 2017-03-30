package crc.modeler.infrastructure;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class CommandHandlerTest {

    private static final long AGGREGATE_ID = 10L;

    @Test
    public void should_append_event_created_by_decide_for_command() throws Exception {
        Event nextEvent = Event.createEvent("EventOccured", AGGREGATE_ID);
        List<Event> newEvents = new ArrayList<>();
        EventStore eventStore = createEventStore(Stream.empty(), newEvents::add);
        CommandHandler commandHandler = new CommandHandler(eventStore);

        commandHandler.handleCommand(
                null,
                state -> Stream.of(nextEvent),
                (state, event) -> state,
                (state1, state2) -> state1);

        assertThat(newEvents).containsExactly(nextEvent);
    }

    @Test
    public void should_call_command_decide_with_initial_state_when_not_past_event_in_store() throws Exception {

        Event nextEvent = Event.createEvent("EventOccured", AGGREGATE_ID);
        List<Event> newEvents = new ArrayList<>();
        EventStore eventStore = createEventStore(Stream.empty(), newEvents::add);
        CommandHandler commandHandler = new CommandHandler(eventStore);

        commandHandler.handleCommand(
                24L,
                (Long state) -> state == 24L ? Stream.of(nextEvent) : Stream.<Event>empty(),
                (state, event) -> state,
                (state1, state2) -> state1);

        assertThat(newEvents).containsExactly(nextEvent);
    }

    @Test
    public void should_call_command_decide_with_state_build_with_initial_state_and_past_event_in_store() throws Exception {

        Event pastEvent = Event.createEvent("PastEventOccured", AGGREGATE_ID);
        Event nextEvent = Event.createEvent("EventOccured", AGGREGATE_ID);
        List<Event> newEvents = new ArrayList<>();
        EventStore eventStore = createEventStore(Stream.of(pastEvent), newEvents::add);
        CommandHandler commandHandler = new CommandHandler(eventStore);

        commandHandler.handleCommand(
                24L,
                (Long state) -> state == 34L ? Stream.of(nextEvent) : Stream.<Event>empty(),
                (state, event) -> Objects.equals("PastEventOccured", event.getEventType()) ? state + 10L : state,
                (state1, state2) -> state1);

        assertThat(newEvents).containsExactly(nextEvent);
    }

    @Test
    public void should_do_nothing_when_command_the_handle_is_null() throws Exception {
        EventStore eventStore = createEventStore(Stream.empty(), event -> {
        });
        CommandHandler commandHandler = new CommandHandler(eventStore);

        commandHandler.handleCommand(null, null,
                (state, event) -> state, (state1, state2) -> state1);

    }

    private EventStore createEventStore(Stream<Event> passEvents, Consumer<Event> appender) {
        return new EventStore() {
            @Override
            public Stream<Event> readEvents() {
                return passEvents;
            }

            @Override
            public void appendEvents(Stream<Event> nextEvents) {
                nextEvents.forEach(appender);
            }
        };
    }
}