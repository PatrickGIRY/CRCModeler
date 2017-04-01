package crc.modeler.infrastructure;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;

public class FileEventStore implements EventStore {
    private static final Charset CHARSET = Charset.forName("UTF-8");
    private final Path path;
    private Pattern FIELD_SEPARATOR_PATTERN = Pattern.compile("\\|");

    public FileEventStore(Path path) {
        this.path = path;
    }

    @Override
    public Stream<Event> readEvents() {
        if (Files.exists(path)) {
            try {
                return Files.lines(path, CHARSET).map(this::toEvent);
            } catch (IOException ioe) {
                throw new UncheckedIOException(ioe);
            }
        } else {
            return Stream.empty();
        }
    }

    private Event toEvent(String line) {
        String[] fields = FIELD_SEPARATOR_PATTERN.split(line);
        return Event.createEvent(fields[1], UUID.fromString(fields[0]), null);
    }

    @Override
    public void appendEvents(Collection<Event> newEvents) {
        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(path, CHARSET, CREATE, APPEND)) {
            newEvents.stream().map(this::toRecord).forEach(record -> {
                try {
                    bufferedWriter.write(record);
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            });
        } catch (IOException ioe) {
            throw new UncheckedIOException(ioe);
        }
    }

    private String toRecord(Event event) {
        return Stream.of(event.getAggregateId(), event.getEventType(), event.getData())
                .map(v -> v != null ? String.valueOf(v) : "")
                .collect(Collectors.joining("|", "", "\n"));
    }
}
