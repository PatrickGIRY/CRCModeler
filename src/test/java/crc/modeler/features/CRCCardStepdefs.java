package crc.modeler.features;

import crc.modeler.domain.ClassName;
import cucumber.api.java8.En;

import static crc.modeler.domain.CRCCard.aCRCCard;
import static org.assertj.core.api.Assertions.assertThat;

public class CRCCardStepdefs implements En {

    public CRCCardStepdefs(CurrentCRCCard currentCRCCard) {
        Given("^the class name of the default CRC card$", () ->
                currentCRCCard.setCrcCard(aCRCCard().withClassName(ClassName.of("Book")).build()));

        Then("^the class name should be (\\w+)$", (String className) ->
                currentCRCCard.doWith(crcCard ->
                        assertThat(crcCard.hasClassName(ClassName.of(className))).isTrue()));
    }
}
