package crc.modeler.application;

import crc.modeler.domain.CRCCardId;
import crc.modeler.domain.ClassName;
import crc.modeler.domain.EventType;
import crc.modeler.infrastructure.Command;
import crc.modeler.infrastructure.Event;

import java.util.stream.Stream;

public class AddClassNameToCRCCard implements Command<CRCCardId> {
    private final CRCCardId crcCardId;
    private final ClassName className;

    public AddClassNameToCRCCard(CRCCardId crcCardId, ClassName className) {
        this.crcCardId = crcCardId;
        this.className = className;
    }

    @Override
    public Stream<Event> decide(CRCCardId state) {
        return Stream.of(Event.createEvent(EventType.CRCCardClassNameAdded, crcCardId, className));
    }
}
