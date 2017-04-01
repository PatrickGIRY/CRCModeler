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
import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

public class Main {


    public static void main(String[] args) {

        CommandHandler commandHandler = new CommandHandler();
        EventStore eventStore = new FileEventStore(Paths.get("data.eventstore"));
        EventPrinter eventPrinter = new EventPrinter();

        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in))) {
            do {
                System.out.print("$ ");
                final String line = bufferedReader.readLine();
                if (Objects.equals("new", line)) {
                    Collection<Event> newEvents = commandHandler.handleCommand(null, new CreateCRCCard(CRCCardId.nextCRCCardId()),
                            eventStore,
                            (state, event) -> state, (state1, state2) -> state1);
                    newEvents.forEach(eventPrinter::print);
                }
                if (Objects.equals("quit", line)) {
                    System.exit(0);
                }

            } while (true);
        } catch (IOException ioe) {
            throw new UncheckedIOException(ioe);
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
