package crc.modeler.infrastructure;

import java.util.Collection;
import java.util.Collections;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.stream.Stream;

public class CommandHandler {
    public <T> Collection<Event> handleCommand(T initialState, Command<T> command, EventStore eventStore,
                                               BiFunction<T, Event, T> evolve, BinaryOperator<T> statesCombiner) {
        if (command != null) {
            Stream<Event> pastEvents = eventStore.readEvents();
            T state = pastEvents.reduce(initialState, evolve, statesCombiner);
            Collection<Event> newEvents = command.decide(state);
            eventStore.appendEvents(newEvents);
            return newEvents;
        } else {
            return Collections.emptyList();
        }
    }

}
