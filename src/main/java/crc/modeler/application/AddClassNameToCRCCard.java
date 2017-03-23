package crc.modeler.application;

import crc.modeler.common.Result;
import crc.modeler.domain.CRCCardId;
import crc.modeler.domain.ClassName;
import crc.modeler.infrastructure.Command;
import crc.modeler.infrastructure.Event;

import java.util.stream.Stream;

import static crc.modeler.domain.EventType.CRCCardClassNameAdded;
import static crc.modeler.domain.EventType.CRCCardClassNameRejected;

public class AddClassNameToCRCCard implements Command<CRCCardId> {
    private final CRCCardId crcCardId;
    private final Result<ClassName> className;

    public AddClassNameToCRCCard(CRCCardId crcCardId, Result<ClassName> className) {
        this.crcCardId = crcCardId;
        this.className = className;
    }

    @Override
    public Stream<Event> decide(CRCCardId state) {
        return className
                .map(validatedClassName ->
                        Stream.of(Event.createEvent(CRCCardClassNameAdded, crcCardId, validatedClassName)))
                .mapOnFailure(error ->
                        Stream.of(Event.createEvent(CRCCardClassNameRejected, crcCardId, error.getMessage())));

    }
}
