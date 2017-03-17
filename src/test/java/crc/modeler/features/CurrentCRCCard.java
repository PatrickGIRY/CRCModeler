package crc.modeler.features;

import crc.modeler.domain.CRCCard;

import java.util.Objects;
import java.util.function.Consumer;

public class CurrentCRCCard {

    private crc.modeler.domain.CRCCard crcCard;

    public void setCrcCard(CRCCard crcCard) {
        this.crcCard = crcCard;
    }

    public void doWith(Consumer<CRCCard> crcCardConsumer) {
        Objects.requireNonNull(crcCardConsumer).accept(crcCard);
    }
}
