package com.yesevi.egitim.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.yesevi.egitim.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EgitimTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Egitim.class);
        Egitim egitim1 = new Egitim();
        egitim1.setId(1L);
        Egitim egitim2 = new Egitim();
        egitim2.setId(egitim1.getId());
        assertThat(egitim1).isEqualTo(egitim2);
        egitim2.setId(2L);
        assertThat(egitim1).isNotEqualTo(egitim2);
        egitim1.setId(null);
        assertThat(egitim1).isNotEqualTo(egitim2);
    }
}
