package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AccountRefTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AccountRef.class);
        AccountRef accountRef1 = new AccountRef();
        accountRef1.setId(1L);
        AccountRef accountRef2 = new AccountRef();
        accountRef2.setId(accountRef1.getId());
        assertThat(accountRef1).isEqualTo(accountRef2);
        accountRef2.setId(2L);
        assertThat(accountRef1).isNotEqualTo(accountRef2);
        accountRef1.setId(null);
        assertThat(accountRef1).isNotEqualTo(accountRef2);
    }
}
