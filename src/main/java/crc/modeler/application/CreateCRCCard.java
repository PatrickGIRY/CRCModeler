package crc.modeler.application;

import crc.modeler.domain.CRCCardId;
import crc.modeler.domain.EventType;
import crc.modeler.infrastructure.Command;
import crc.modeler.infrastructure.Event;

import java.util.Collection;
import java.util.Collections;

public class CreateCRCCard implements Command<CRCCardId> {
    private final CRCCardId crcCardId;

    public CreateCRCCard(CRCCardId crcCardId) {
        this.crcCardId = crcCardId;
    }

    @Override
    public Collection<Event> decide(CRCCardId state) {
        return Collections.singletonList(Event.createEvent(EventType.CRCCardCreated, crcCardId));
    }
}
