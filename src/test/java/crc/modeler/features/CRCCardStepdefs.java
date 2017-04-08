package crc.modeler.features;

import crc.modeler.application.CreateCRCCard;
import crc.modeler.common.Result;
import crc.modeler.domain.CRCCardId;
import crc.modeler.domain.ClassName;
import crc.modeler.domain.InvalidClassNameException;
import crc.modeler.infrastructure.CommandHandler;
import crc.modeler.infrastructure.Event;
import cucumber.api.java8.En;

import java.util.stream.Stream;

import static crc.modeler.domain.EventType.CRCCardCreated;
import static crc.modeler.domain.EventType.CRCCardRejected;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

public class CRCCardStepdefs implements En {

    private Result<ClassName> givenClassNameResult;

    public CRCCardStepdefs(CommandHandler commandHandler, CurrentEventStore eventStore) {

        CRCCardId newCRCCardId = CRCCardId.nextCRCCardId();

        Given("^(.+) (?:\\ba valid\\b|\\ban invalid\\b) class name$", (String classNameText) -> {
            givenClassNameResult = ClassName.of(classNameText);
        });


        When("^I create a new CRC card$", () -> {
            commandHandler.handleCommand(
                    null,
                    new CreateCRCCard(newCRCCardId, givenClassNameResult),
                    (state, event) -> state,
                    (state1, state2) -> state1);
        });


        Then("^the new CRC card should created with the next CRC card id and the given class name$", () -> {
            Stream<Event> pastEvents = eventStore.readEvents();
            assertThat(pastEvents).extracting(Event::getAggregateId, Event::getEventType, Event::getData)
                    .containsExactly(
                            tuple(newCRCCardId, CRCCardCreated, givenClassNameResult.successValue()));
        });

        Then("^the new CRC card should rejected with the (?:'.+') error containing the invalid class name (.+)$", (String classNameText) -> {
            Stream<Event> pastEvents = eventStore.readEvents();
            assertThat(pastEvents).extracting(Event::getAggregateId, Event::getEventType, Event::getData)
                    .containsExactly(
                            tuple(newCRCCardId, CRCCardRejected, new InvalidClassNameException(classNameText)));
        });
    }
}
