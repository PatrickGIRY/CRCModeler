package crc.modeler.domain;

import org.junit.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class CRCCardIdTest {

    @Test
    public void should_return_the_next_CRC_card_id() throws Exception {
        assertThat(CRCCardId.nextCRCCardId()).isNotNull();
    }

    @Test
    public void a_next_CRC_card_id_should_not_be_equals_to_the_previous_one() throws Exception {
        CRCCardId previousCRCCardId = CRCCardId.nextCRCCardId();
        CRCCardId nextCRCCardId = CRCCardId.nextCRCCardId();
        assertThat(nextCRCCardId).isNotEqualTo(previousCRCCardId);
    }

    @Test
    public void should_create_a_CRC_card_id_from_an_UUID() throws Exception {
        UUID uuid = UUID.randomUUID();
        assertThat(CRCCardId.fromUUID(uuid)).isNotNull();
    }
}