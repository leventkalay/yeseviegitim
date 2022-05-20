package com.yesevi.egitim.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.yesevi.egitim.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EgitmenTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Egitmen.class);
        Egitmen egitmen1 = new Egitmen();
        egitmen1.setId(1L);
        Egitmen egitmen2 = new Egitmen();
        egitmen2.setId(egitmen1.getId());
        assertThat(egitmen1).isEqualTo(egitmen2);
        egitmen2.setId(2L);
        assertThat(egitmen1).isNotEqualTo(egitmen2);
        egitmen1.setId(null);
        assertThat(egitmen1).isNotEqualTo(egitmen2);
    }
}
