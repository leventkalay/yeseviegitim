package com.yesevi.egitim.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.yesevi.egitim.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DuyuruTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Duyuru.class);
        Duyuru duyuru1 = new Duyuru();
        duyuru1.setId(1L);
        Duyuru duyuru2 = new Duyuru();
        duyuru2.setId(duyuru1.getId());
        assertThat(duyuru1).isEqualTo(duyuru2);
        duyuru2.setId(2L);
        assertThat(duyuru1).isNotEqualTo(duyuru2);
        duyuru1.setId(null);
        assertThat(duyuru1).isNotEqualTo(duyuru2);
    }
}
