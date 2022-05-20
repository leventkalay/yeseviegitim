package com.yesevi.egitim.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.yesevi.egitim.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TakvimTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Takvim.class);
        Takvim takvim1 = new Takvim();
        takvim1.setId(1L);
        Takvim takvim2 = new Takvim();
        takvim2.setId(takvim1.getId());
        assertThat(takvim1).isEqualTo(takvim2);
        takvim2.setId(2L);
        assertThat(takvim1).isNotEqualTo(takvim2);
        takvim1.setId(null);
        assertThat(takvim1).isNotEqualTo(takvim2);
    }
}
