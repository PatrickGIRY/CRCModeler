package crc.modeler.infrastructure;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

public class CommandHandler {
    public <T> void handleCommand(T initialState, Command<T> command, EventStore eventStore,
                                  BiFunction<T, Event, T> evolve) {
        if (command != null) {
            Stream<Event> pastEvents = eventStore.readEvents();
            T state = buildState(initialState, evolve, pastEvents);
            Stream<Event> newEvents = command.decide(state);
            eventStore.appendEvents(newEvents);
        }
    }

    private <T> T buildState(T initialState, BiFunction<T, Event, T> evolve, Stream<Event> pastEvents) {
        Function<Event, Function<T, T>> mapper = event -> state -> evolve.apply(state, event);
        Function<T, T> stateBuilder = pastEvents.map(mapper).reduce(Function.identity(), Function::andThen);
        return stateBuilder.apply(initialState);
    }


}
