package crc.modeler.features;

import crc.modeler.domain.CRCCardId;

import java.util.UUID;

public class Context {

    private UUID defaultUUID() {
        return UUID.fromString("9e8ad38e-3d58-4992-9c22-4da5bb8e8790");
    }

    CRCCardId defaultCRCCardId() {
        return CRCCardId.from(defaultUUID());
    }
}
