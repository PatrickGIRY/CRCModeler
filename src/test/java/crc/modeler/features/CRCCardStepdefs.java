package crc.modeler.features;

import crc.modeler.application.AddClassNameToCRCCard;
import crc.modeler.application.CreateCRCCard;
import crc.modeler.common.Result;
import crc.modeler.domain.CRCCardId;
import crc.modeler.domain.ClassName;
import crc.modeler.infrastructure.CommandHandler;
import crc.modeler.infrastructure.Event;
import cucumber.api.java8.En;

import java.util.UUID;
import java.util.stream.Stream;

import static crc.modeler.domain.EventType.CRCCardClassNameAdded;
import static crc.modeler.domain.EventType.CRCCardClassNameRejected;
import static crc.modeler.domain.EventType.CRCCardCreated;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

public class CRCCardStepdefs implements En {

    public CRCCardStepdefs(CommandHandler commandHandler, CurrentEventStore eventStore, Context context) {

        CRCCardId newCRCCardId = CRCCardId.nextCRCCardId();

        Given("^a default CRC card$", () ->
                commandHandler.handleCommand(null, new CreateCRCCard(context.defaultCRCCardId()),
                        eventStore,
                        (state, event) -> state,
                        (state1, state2) -> state1));

        Then("^the CRC card id should be ([a-f|0-9|\\-]+)$", (UUID uuid) -> {
            Stream<Event> pastEvents = eventStore.readEvents();
            assertThat(pastEvents).extracting(Event::getAggregateId, Event::getEventType, Event::getData)
                    .containsExactly(tuple(CRCCardId.fromUUID(uuid), CRCCardCreated, null));
        });

        When("^I create a new CRC card$", () ->
                commandHandler.handleCommand(null, new CreateCRCCard(newCRCCardId), eventStore,
                        (state, event) -> state, (state1, state2) -> state1));

        Then("^the new CRC card should created with the next CRC card id$", () -> {
            Stream<Event> pastEvents = eventStore.readEvents();
            assertThat(pastEvents)
                    .extracting(Event::getAggregateId, Event::getEventType, Event::getData)
                    .containsExactly(tuple(newCRCCardId, CRCCardCreated, null));
        });

        When("^I add class name (.*) to the CRC card$", (String value) -> {
            Result<ClassName> className = ClassName.of(value);
            commandHandler.handleCommand(null,
                    new AddClassNameToCRCCard(context.defaultCRCCardId(), className),
                    eventStore,
                    (state, event) -> state, (state1, state2) -> state1);
        });

        Then("^The class name (.*) should be added to the default CRC card$", (String value) -> {
            Stream<Event> pastEvents = eventStore.readEvents();
            assertThat(pastEvents).extracting(Event::getAggregateId, Event::getEventType, Event::getData)
                    .containsExactly(
                            tuple(context.defaultCRCCardId(), CRCCardCreated, null),
                            tuple(context.defaultCRCCardId(), CRCCardClassNameAdded, ClassName.of(value).successValue()));
        });

        Then("^the class name (.*) should be rejected with the message (.+)$", (String className, String message) -> {
            Stream<Event> pastEvents = eventStore.readEvents();
            assertThat(pastEvents).extracting(Event::getAggregateId, Event::getEventType, Event::getData)
                    .containsExactly(
                            tuple(context.defaultCRCCardId(), CRCCardCreated, null),
                            tuple(context.defaultCRCCardId(), CRCCardClassNameRejected, message));
        });
    }
}
