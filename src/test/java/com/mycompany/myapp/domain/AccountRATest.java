package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AccountRATest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AccountRA.class);
        AccountRA accountRA1 = new AccountRA();
        accountRA1.setId(1L);
        AccountRA accountRA2 = new AccountRA();
        accountRA2.setId(accountRA1.getId());
        assertThat(accountRA1).isEqualTo(accountRA2);
        accountRA2.setId(2L);
        assertThat(accountRA1).isNotEqualTo(accountRA2);
        accountRA1.setId(null);
        assertThat(accountRA1).isNotEqualTo(accountRA2);
    }
}
