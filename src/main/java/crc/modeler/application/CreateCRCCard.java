package crc.modeler.application;

import crc.modeler.domain.CRCCardId;
import crc.modeler.domain.EventType;
import crc.modeler.infrastructure.Command;
import crc.modeler.infrastructure.Event;

import java.util.stream.Stream;

public class CreateCRCCard implements Command<CRCCardId> {
    private final CRCCardId crcCardId;

    public CreateCRCCard(CRCCardId crcCardId) {
        this.crcCardId = crcCardId;
    }

    @Override
    public Stream<Event> decide(CRCCardId state) {
        return Stream.of(Event.createEvent(EventType.CRCCardCreated, crcCardId));
    }
}
