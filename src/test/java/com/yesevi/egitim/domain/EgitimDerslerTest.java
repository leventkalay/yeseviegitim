package com.yesevi.egitim.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.yesevi.egitim.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EgitimDerslerTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EgitimDersler.class);
        EgitimDersler egitimDersler1 = new EgitimDersler();
        egitimDersler1.setId(1L);
        EgitimDersler egitimDersler2 = new EgitimDersler();
        egitimDersler2.setId(egitimDersler1.getId());
        assertThat(egitimDersler1).isEqualTo(egitimDersler2);
        egitimDersler2.setId(2L);
        assertThat(egitimDersler1).isNotEqualTo(egitimDersler2);
        egitimDersler1.setId(null);
        assertThat(egitimDersler1).isNotEqualTo(egitimDersler2);
    }
}
