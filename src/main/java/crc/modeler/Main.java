package crc.modeler;

import crc.modeler.application.CreateCRCCard;
import crc.modeler.domain.CRCCardId;
import crc.modeler.domain.EventType;
import crc.modeler.infrastructure.CommandHandler;
import crc.modeler.infrastructure.Event;
import crc.modeler.infrastructure.EventStore;
import crc.modeler.infrastructure.FileEventStore;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) {

        EventStore eventStore = new FileEventStore(Paths.get("data.eventstore"));
        EventPrinter eventPrinter = new EventPrinter();
        CommandHandler commandHandler = new CommandHandler(eventStore);

        List<CommandMatcher> commandMatchers = Arrays.asList(
                new CommandMatcher(Pattern.compile("^new$"), line -> {
                    Collection<Event> newEvents = commandHandler.handleCommand(
                            null,
                            new CreateCRCCard(CRCCardId.nextCRCCardId()),
                            (state, event) -> state,
                            (state1, state2) -> state1);
                    newEvents.forEach(eventPrinter::print);
                }),
                new CommandMatcher(Pattern.compile("^quit$"), line -> System.exit(0))
        );
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in))) {
            do {
                System.out.print("$ ");
                final String line = bufferedReader.readLine();
                commandMatchers.stream().filter(cm -> cm.isSelected(line)).forEach(cm -> cm.execute(line));
            } while (true);
        } catch (IOException ioe) {
            throw new UncheckedIOException(ioe);
        }

    }

    private static class CommandMatcher {
        private final Predicate<String> selector;
        private final Executable action;

        private CommandMatcher(Pattern selector, Executable action) {
            this.selector = selector.asPredicate();
            this.action = action;
        }

        private boolean isSelected(String line) {
            return selector.test(line);
        }

        private void execute(String line) {
            action.execute(line);
        }

        @FunctionalInterface
        private interface Executable {
            void execute(String line);
        }
    }

    private static class EventPrinter {

        private Map<EventType, Consumer<Event>> eventPrinterByType =
                new EnumMap<EventType, Consumer<Event>>(EventType.class) {{
                    put(EventType.CRCCardCreated, event ->
                            System.out.printf("CRC card %s created\n", event.getAggregateId()));
                }};

        void print(Event event) {
            eventPrinterByType.getOrDefault(event.getEventType(EventType.class), evt -> {
            }).accept(event);
        }


    }
}
