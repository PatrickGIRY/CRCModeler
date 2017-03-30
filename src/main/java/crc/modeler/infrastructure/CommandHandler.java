package crc.modeler.infrastructure;

import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.stream.Stream;

public class CommandHandler {

    private final EventStore eventStore;

    public CommandHandler(EventStore eventStore) {
        this.eventStore = eventStore;
    }

    public <T> void handleCommand(T initialState, Command<T> command,
                                  BiFunction<T, Event, T> evolve, BinaryOperator<T> statesCombiner) {
        if (command != null) {
            Stream<Event> pastEvents = this.eventStore.readEvents();
            T state = pastEvents.reduce(initialState, evolve, statesCombiner);
            Stream<Event> newEvents = command.decide(state);
            this.eventStore.appendEvents(newEvents);
        }
    }


}
