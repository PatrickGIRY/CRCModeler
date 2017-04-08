package crc.modeler;

import crc.modeler.application.AddClassNameToCRCCard;
import crc.modeler.application.CreateCRCCard;
import crc.modeler.domain.CRCCardId;
import crc.modeler.domain.ClassName;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public class Main {

    private static final String CURRENT_CRC_CARD_ID_KEY = "currentCRCCardId";

    public static void main(String[] args) {

        EventStore eventStore = new FileEventStore(Paths.get("data.eventstore"));
        EventPrinter eventPrinter = new EventPrinter();
        CommandHandler commandHandler = new CommandHandler(eventStore);
        Map<String, CRCCardId> userContext = new HashMap<>();

        List<CommandMatcher> commandMatchers = Arrays.asList(
                new CommandMatcher(Pattern.compile("^new$"), groups -> {
                    Collection<Event> newEvents = commandHandler.handleCommand(
                            null,
                            new CreateCRCCard(CRCCardId.nextCRCCardId()),
                            (state, event) -> state,
                            (state1, state2) -> state1);
                    newEvents.forEach(event -> {
                        userContext.put(CURRENT_CRC_CARD_ID_KEY, event.getAggregateId(CRCCardId.class));
                        eventPrinter.print(event);
                    });
                }),
                new CommandMatcher(Pattern.compile("^name (.*)$"), groups -> {
                    Collection<Event> newEvents = commandHandler.handleCommand(
                            null,
                            new AddClassNameToCRCCard(userContext.get(CURRENT_CRC_CARD_ID_KEY), ClassName.of(groups[1])),
                            (state, event) -> state,
                            (state1, state2) -> state1);
                    newEvents.forEach(eventPrinter::print);
                }),
                new CommandMatcher(Pattern.compile("^quit$"), groups -> System.exit(0))
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
        private final Pattern selector;
        private final Executable action;

        private CommandMatcher(Pattern selector, Executable action) {
            this.selector = selector;
            this.action = action;
        }

        private boolean isSelected(String line) {
            return selector.asPredicate().test(line);
        }

        private void execute(String line) {
            Matcher matcher = selector.matcher(line);
            final String[] groups = matcher.lookingAt()
                    ? IntStream.rangeClosed(0, matcher.groupCount()).mapToObj(matcher::group).toArray(String[]::new)
                    : new String[]{line};
            action.execute(groups);
        }

        @FunctionalInterface
        private interface Executable {
            void execute(String[] groups);
        }
    }

    private static class EventPrinter {

        private Map<EventType, Consumer<Event>> eventPrinterByType =
                new EnumMap<EventType, Consumer<Event>>(EventType.class) {{
                    put(EventType.CRCCardCreated, event ->
                            System.out.printf("CRC card %s created\n", event.getAggregateId()));
                    put(EventType.CRCCardClassNameAdded, event ->
                            System.out.printf("CRC card %s named %s\n", event.getAggregateId(), event.getData()));
                    put(EventType.CRCCardClassNameRejected, event ->
                            System.out.printf("CRC card %s, ERROR: %s\n", event.getAggregateId(), event.getData()));
                }};

        void print(Event event) {
            eventPrinterByType.getOrDefault(event.getEventType(EventType.class), evt -> {
            }).accept(event);
        }


    }
}
