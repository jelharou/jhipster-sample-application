package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PartyIdTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PartyId.class);
        PartyId partyId1 = new PartyId();
        partyId1.setId(1L);
        PartyId partyId2 = new PartyId();
        partyId2.setId(partyId1.getId());
        assertThat(partyId1).isEqualTo(partyId2);
        partyId2.setId(2L);
        assertThat(partyId1).isNotEqualTo(partyId2);
        partyId1.setId(null);
        assertThat(partyId1).isNotEqualTo(partyId2);
    }
}
