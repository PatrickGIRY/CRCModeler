package crc.modeler.application;

import crc.modeler.common.Result;
import crc.modeler.domain.CRCCardId;
import crc.modeler.domain.ClassName;
import crc.modeler.infrastructure.Command;
import crc.modeler.infrastructure.Event;

import java.util.Collection;
import java.util.Collections;

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
    public Collection<Event> decide(CRCCardId state) {
        return Collections.singletonList(className
                .map(validatedClassName ->
                        Event.createEvent(CRCCardClassNameAdded, crcCardId, validatedClassName))
                .mapOnFailure(error ->
                        Event.createEvent(CRCCardClassNameRejected, crcCardId, error.getMessage())));

    }
}
