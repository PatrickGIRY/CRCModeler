package crc.modeler.application;

import crc.modeler.common.Result;
import crc.modeler.domain.CRCCardId;
import crc.modeler.domain.ClassName;
import crc.modeler.infrastructure.Command;
import crc.modeler.infrastructure.Event;

import java.util.Collection;
import java.util.Collections;

import static crc.modeler.domain.EventType.CRCCardCreated;
import static crc.modeler.domain.EventType.CRCCardRejected;

public class CreateCRCCard implements Command<CRCCardId> {
    private final CRCCardId crcCardId;
    private final Result<ClassName> classNameResult;

    public CreateCRCCard(CRCCardId crcCardId, Result<ClassName> classNameResult) {
        this.crcCardId = crcCardId;
        this.classNameResult = classNameResult;
    }

    @Override
    public Collection<Event> decide(CRCCardId state) {
        return Collections.singletonList(classNameResult
                .map(className -> Event.createEvent(CRCCardCreated, crcCardId, className))
                .mapOnFailure(error -> Event.createEvent(CRCCardRejected, crcCardId, error)));
    }
}
