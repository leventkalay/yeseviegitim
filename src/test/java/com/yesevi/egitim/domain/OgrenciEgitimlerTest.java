package com.yesevi.egitim.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.yesevi.egitim.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OgrenciEgitimlerTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OgrenciEgitimler.class);
        OgrenciEgitimler ogrenciEgitimler1 = new OgrenciEgitimler();
        ogrenciEgitimler1.setId(1L);
        OgrenciEgitimler ogrenciEgitimler2 = new OgrenciEgitimler();
        ogrenciEgitimler2.setId(ogrenciEgitimler1.getId());
        assertThat(ogrenciEgitimler1).isEqualTo(ogrenciEgitimler2);
        ogrenciEgitimler2.setId(2L);
        assertThat(ogrenciEgitimler1).isNotEqualTo(ogrenciEgitimler2);
        ogrenciEgitimler1.setId(null);
        assertThat(ogrenciEgitimler1).isNotEqualTo(ogrenciEgitimler2);
    }
}
