package crc.modeler.domain;

import java.util.Objects;
import java.util.UUID;

public class CRCCardId {
    private final UUID uuid;

    private CRCCardId(UUID uuid) {
        this.uuid = uuid;
    }

    public static CRCCardId nextCRCCardId() {
        return new CRCCardId(UUID.randomUUID());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CRCCardId crcCardId = (CRCCardId) o;
        return Objects.equals(uuid, crcCardId.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }

    @Override
    public String toString() {
        return uuid.toString();
    }
}
