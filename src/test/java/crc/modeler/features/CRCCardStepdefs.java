package crc.modeler.features;

import crc.modeler.application.CreateCRCCard;
import crc.modeler.domain.CRCCardId;
import crc.modeler.domain.EventType;
import crc.modeler.infrastructure.CommandHandler;
import crc.modeler.infrastructure.Event;
import cucumber.api.java8.En;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class CRCCardStepdefs implements En {

    public CRCCardStepdefs(CommandHandler commandHandler, CurrentEventStore eventStore) {

        CRCCardId crcCardId = CRCCardId.nextCRCCardId();

        When("^I create a new CRC card$", () ->
                commandHandler.handleCommand(null, new CreateCRCCard(crcCardId), eventStore,
                        (state, event) -> state));

        Then("^the new CRC card should created with the next CRC card id$", () -> {
            Stream<Event> pastEvents = eventStore.readEvents();
            assertThat(pastEvents.filter(event -> event.hasAggregateId(crcCardId))
                    .anyMatch(event -> event.hasType(EventType.CRCCardCreated))).isTrue();
        });

    }
}
