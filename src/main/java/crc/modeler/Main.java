package crc.modeler;

import crc.modeler.application.CreateCRCCard;
import crc.modeler.domain.CRCCardId;
import crc.modeler.infrastructure.CommandHandler;
import crc.modeler.infrastructure.EventStore;
import crc.modeler.infrastructure.FileEventStore;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.file.Paths;
import java.util.Objects;

public class Main {


    public static void main(String[] args) {

        CommandHandler commandHandler = new CommandHandler();
        EventStore eventStore = new FileEventStore(Paths.get("data.eventstore"));

        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in))) {
            do {
                System.out.print("$ ");
                final String line = bufferedReader.readLine();
                if (Objects.equals("new", line)) {
                    commandHandler.handleCommand(null, new CreateCRCCard(CRCCardId.nextCRCCardId()),
                            eventStore,
                            (state, event) -> state, (state1, state2) -> state1);
                }
                if (Objects.equals("quit", line)) {
                    System.exit(0);
                }

            } while (true);
        } catch (IOException ioe) {
            throw new UncheckedIOException(ioe);
        }

    }


}
