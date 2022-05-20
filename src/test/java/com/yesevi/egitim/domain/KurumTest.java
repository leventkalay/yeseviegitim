package com.yesevi.egitim.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.yesevi.egitim.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class KurumTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Kurum.class);
        Kurum kurum1 = new Kurum();
        kurum1.setId(1L);
        Kurum kurum2 = new Kurum();
        kurum2.setId(kurum1.getId());
        assertThat(kurum1).isEqualTo(kurum2);
        kurum2.setId(2L);
        assertThat(kurum1).isNotEqualTo(kurum2);
        kurum1.setId(null);
        assertThat(kurum1).isNotEqualTo(kurum2);
    }
}
