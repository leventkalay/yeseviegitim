package com.yesevi.egitim.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.yesevi.egitim.IntegrationTest;
import com.yesevi.egitim.domain.Egitim;
import com.yesevi.egitim.repository.EgitimRepository;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link EgitimResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EgitimResourceIT {

    private static final String DEFAULT_EGITIM_BASLIK = "AAAAAAAAAA";
    private static final String UPDATED_EGITIM_BASLIK = "BBBBBBBBBB";

    private static final String DEFAULT_EGITIM_ALT_BASLIK = "AAAAAAAAAA";
    private static final String UPDATED_EGITIM_ALT_BASLIK = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_EGITIM_BASLAMA_TARIHI = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_EGITIM_BASLAMA_TARIHI = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_EGITIM_BITIS_TARIHI = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_EGITIM_BITIS_TARIHI = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_DERS_SAYISI = 1;
    private static final Integer UPDATED_DERS_SAYISI = 2;

    private static final Float DEFAULT_EGITIM_SURESI = 1F;
    private static final Float UPDATED_EGITIM_SURESI = 2F;

    private static final String DEFAULT_EGITIM_YERI = "AAAAAAAAAA";
    private static final String UPDATED_EGITIM_YERI = "BBBBBBBBBB";

    private static final Float DEFAULT_EGITIM_PUANI = 1F;
    private static final Float UPDATED_EGITIM_PUANI = 2F;

    private static final Boolean DEFAULT_AKTIF = false;
    private static final Boolean UPDATED_AKTIF = true;

    private static final String ENTITY_API_URL = "/api/egitims";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EgitimRepository egitimRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEgitimMockMvc;

    private Egitim egitim;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Egitim createEntity(EntityManager em) {
        Egitim egitim = new Egitim()
            .egitimBaslik(DEFAULT_EGITIM_BASLIK)
            .egitimAltBaslik(DEFAULT_EGITIM_ALT_BASLIK)
            .egitimBaslamaTarihi(DEFAULT_EGITIM_BASLAMA_TARIHI)
            .egitimBitisTarihi(DEFAULT_EGITIM_BITIS_TARIHI)
            .dersSayisi(DEFAULT_DERS_SAYISI)
            .egitimSuresi(DEFAULT_EGITIM_SURESI)
            .egitimYeri(DEFAULT_EGITIM_YERI)
            .egitimPuani(DEFAULT_EGITIM_PUANI)
            .aktif(DEFAULT_AKTIF);
        return egitim;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Egitim createUpdatedEntity(EntityManager em) {
        Egitim egitim = new Egitim()
            .egitimBaslik(UPDATED_EGITIM_BASLIK)
            .egitimAltBaslik(UPDATED_EGITIM_ALT_BASLIK)
            .egitimBaslamaTarihi(UPDATED_EGITIM_BASLAMA_TARIHI)
            .egitimBitisTarihi(UPDATED_EGITIM_BITIS_TARIHI)
            .dersSayisi(UPDATED_DERS_SAYISI)
            .egitimSuresi(UPDATED_EGITIM_SURESI)
            .egitimYeri(UPDATED_EGITIM_YERI)
            .egitimPuani(UPDATED_EGITIM_PUANI)
            .aktif(UPDATED_AKTIF);
        return egitim;
    }

    @BeforeEach
    public void initTest() {
        egitim = createEntity(em);
    }

    @Test
    @Transactional
    void createEgitim() throws Exception {
        int databaseSizeBeforeCreate = egitimRepository.findAll().size();
        // Create the Egitim
        restEgitimMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(egitim)))
            .andExpect(status().isCreated());

        // Validate the Egitim in the database
        List<Egitim> egitimList = egitimRepository.findAll();
        assertThat(egitimList).hasSize(databaseSizeBeforeCreate + 1);
        Egitim testEgitim = egitimList.get(egitimList.size() - 1);
        assertThat(testEgitim.getEgitimBaslik()).isEqualTo(DEFAULT_EGITIM_BASLIK);
        assertThat(testEgitim.getEgitimAltBaslik()).isEqualTo(DEFAULT_EGITIM_ALT_BASLIK);
        assertThat(testEgitim.getEgitimBaslamaTarihi()).isEqualTo(DEFAULT_EGITIM_BASLAMA_TARIHI);
        assertThat(testEgitim.getEgitimBitisTarihi()).isEqualTo(DEFAULT_EGITIM_BITIS_TARIHI);
        assertThat(testEgitim.getDersSayisi()).isEqualTo(DEFAULT_DERS_SAYISI);
        assertThat(testEgitim.getEgitimSuresi()).isEqualTo(DEFAULT_EGITIM_SURESI);
        assertThat(testEgitim.getEgitimYeri()).isEqualTo(DEFAULT_EGITIM_YERI);
        assertThat(testEgitim.getEgitimPuani()).isEqualTo(DEFAULT_EGITIM_PUANI);
        assertThat(testEgitim.getAktif()).isEqualTo(DEFAULT_AKTIF);
    }

    @Test
    @Transactional
    void createEgitimWithExistingId() throws Exception {
        // Create the Egitim with an existing ID
        egitim.setId(1L);

        int databaseSizeBeforeCreate = egitimRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEgitimMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(egitim)))
            .andExpect(status().isBadRequest());

        // Validate the Egitim in the database
        List<Egitim> egitimList = egitimRepository.findAll();
        assertThat(egitimList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllEgitims() throws Exception {
        // Initialize the database
        egitimRepository.saveAndFlush(egitim);

        // Get all the egitimList
        restEgitimMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(egitim.getId().intValue())))
            .andExpect(jsonPath("$.[*].egitimBaslik").value(hasItem(DEFAULT_EGITIM_BASLIK)))
            .andExpect(jsonPath("$.[*].egitimAltBaslik").value(hasItem(DEFAULT_EGITIM_ALT_BASLIK)))
            .andExpect(jsonPath("$.[*].egitimBaslamaTarihi").value(hasItem(DEFAULT_EGITIM_BASLAMA_TARIHI.toString())))
            .andExpect(jsonPath("$.[*].egitimBitisTarihi").value(hasItem(DEFAULT_EGITIM_BITIS_TARIHI.toString())))
            .andExpect(jsonPath("$.[*].dersSayisi").value(hasItem(DEFAULT_DERS_SAYISI)))
            .andExpect(jsonPath("$.[*].egitimSuresi").value(hasItem(DEFAULT_EGITIM_SURESI.doubleValue())))
            .andExpect(jsonPath("$.[*].egitimYeri").value(hasItem(DEFAULT_EGITIM_YERI)))
            .andExpect(jsonPath("$.[*].egitimPuani").value(hasItem(DEFAULT_EGITIM_PUANI.doubleValue())))
            .andExpect(jsonPath("$.[*].aktif").value(hasItem(DEFAULT_AKTIF.booleanValue())));
    }

    @Test
    @Transactional
    void getEgitim() throws Exception {
        // Initialize the database
        egitimRepository.saveAndFlush(egitim);

        // Get the egitim
        restEgitimMockMvc
            .perform(get(ENTITY_API_URL_ID, egitim.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(egitim.getId().intValue()))
            .andExpect(jsonPath("$.egitimBaslik").value(DEFAULT_EGITIM_BASLIK))
            .andExpect(jsonPath("$.egitimAltBaslik").value(DEFAULT_EGITIM_ALT_BASLIK))
            .andExpect(jsonPath("$.egitimBaslamaTarihi").value(DEFAULT_EGITIM_BASLAMA_TARIHI.toString()))
            .andExpect(jsonPath("$.egitimBitisTarihi").value(DEFAULT_EGITIM_BITIS_TARIHI.toString()))
            .andExpect(jsonPath("$.dersSayisi").value(DEFAULT_DERS_SAYISI))
            .andExpect(jsonPath("$.egitimSuresi").value(DEFAULT_EGITIM_SURESI.doubleValue()))
            .andExpect(jsonPath("$.egitimYeri").value(DEFAULT_EGITIM_YERI))
            .andExpect(jsonPath("$.egitimPuani").value(DEFAULT_EGITIM_PUANI.doubleValue()))
            .andExpect(jsonPath("$.aktif").value(DEFAULT_AKTIF.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingEgitim() throws Exception {
        // Get the egitim
        restEgitimMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewEgitim() throws Exception {
        // Initialize the database
        egitimRepository.saveAndFlush(egitim);

        int databaseSizeBeforeUpdate = egitimRepository.findAll().size();

        // Update the egitim
        Egitim updatedEgitim = egitimRepository.findById(egitim.getId()).get();
        // Disconnect from session so that the updates on updatedEgitim are not directly saved in db
        em.detach(updatedEgitim);
        updatedEgitim
            .egitimBaslik(UPDATED_EGITIM_BASLIK)
            .egitimAltBaslik(UPDATED_EGITIM_ALT_BASLIK)
            .egitimBaslamaTarihi(UPDATED_EGITIM_BASLAMA_TARIHI)
            .egitimBitisTarihi(UPDATED_EGITIM_BITIS_TARIHI)
            .dersSayisi(UPDATED_DERS_SAYISI)
            .egitimSuresi(UPDATED_EGITIM_SURESI)
            .egitimYeri(UPDATED_EGITIM_YERI)
            .egitimPuani(UPDATED_EGITIM_PUANI)
            .aktif(UPDATED_AKTIF);

        restEgitimMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEgitim.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedEgitim))
            )
            .andExpect(status().isOk());

        // Validate the Egitim in the database
        List<Egitim> egitimList = egitimRepository.findAll();
        assertThat(egitimList).hasSize(databaseSizeBeforeUpdate);
        Egitim testEgitim = egitimList.get(egitimList.size() - 1);
        assertThat(testEgitim.getEgitimBaslik()).isEqualTo(UPDATED_EGITIM_BASLIK);
        assertThat(testEgitim.getEgitimAltBaslik()).isEqualTo(UPDATED_EGITIM_ALT_BASLIK);
        assertThat(testEgitim.getEgitimBaslamaTarihi()).isEqualTo(UPDATED_EGITIM_BASLAMA_TARIHI);
        assertThat(testEgitim.getEgitimBitisTarihi()).isEqualTo(UPDATED_EGITIM_BITIS_TARIHI);
        assertThat(testEgitim.getDersSayisi()).isEqualTo(UPDATED_DERS_SAYISI);
        assertThat(testEgitim.getEgitimSuresi()).isEqualTo(UPDATED_EGITIM_SURESI);
        assertThat(testEgitim.getEgitimYeri()).isEqualTo(UPDATED_EGITIM_YERI);
        assertThat(testEgitim.getEgitimPuani()).isEqualTo(UPDATED_EGITIM_PUANI);
        assertThat(testEgitim.getAktif()).isEqualTo(UPDATED_AKTIF);
    }

    @Test
    @Transactional
    void putNonExistingEgitim() throws Exception {
        int databaseSizeBeforeUpdate = egitimRepository.findAll().size();
        egitim.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEgitimMockMvc
            .perform(
                put(ENTITY_API_URL_ID, egitim.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(egitim))
            )
            .andExpect(status().isBadRequest());

        // Validate the Egitim in the database
        List<Egitim> egitimList = egitimRepository.findAll();
        assertThat(egitimList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEgitim() throws Exception {
        int databaseSizeBeforeUpdate = egitimRepository.findAll().size();
        egitim.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEgitimMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(egitim))
            )
            .andExpect(status().isBadRequest());

        // Validate the Egitim in the database
        List<Egitim> egitimList = egitimRepository.findAll();
        assertThat(egitimList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEgitim() throws Exception {
        int databaseSizeBeforeUpdate = egitimRepository.findAll().size();
        egitim.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEgitimMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(egitim)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Egitim in the database
        List<Egitim> egitimList = egitimRepository.findAll();
        assertThat(egitimList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEgitimWithPatch() throws Exception {
        // Initialize the database
        egitimRepository.saveAndFlush(egitim);

        int databaseSizeBeforeUpdate = egitimRepository.findAll().size();

        // Update the egitim using partial update
        Egitim partialUpdatedEgitim = new Egitim();
        partialUpdatedEgitim.setId(egitim.getId());

        partialUpdatedEgitim
            .egitimBaslik(UPDATED_EGITIM_BASLIK)
            .egitimAltBaslik(UPDATED_EGITIM_ALT_BASLIK)
            .egitimSuresi(UPDATED_EGITIM_SURESI)
            .aktif(UPDATED_AKTIF);

        restEgitimMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEgitim.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEgitim))
            )
            .andExpect(status().isOk());

        // Validate the Egitim in the database
        List<Egitim> egitimList = egitimRepository.findAll();
        assertThat(egitimList).hasSize(databaseSizeBeforeUpdate);
        Egitim testEgitim = egitimList.get(egitimList.size() - 1);
        assertThat(testEgitim.getEgitimBaslik()).isEqualTo(UPDATED_EGITIM_BASLIK);
        assertThat(testEgitim.getEgitimAltBaslik()).isEqualTo(UPDATED_EGITIM_ALT_BASLIK);
        assertThat(testEgitim.getEgitimBaslamaTarihi()).isEqualTo(DEFAULT_EGITIM_BASLAMA_TARIHI);
        assertThat(testEgitim.getEgitimBitisTarihi()).isEqualTo(DEFAULT_EGITIM_BITIS_TARIHI);
        assertThat(testEgitim.getDersSayisi()).isEqualTo(DEFAULT_DERS_SAYISI);
        assertThat(testEgitim.getEgitimSuresi()).isEqualTo(UPDATED_EGITIM_SURESI);
        assertThat(testEgitim.getEgitimYeri()).isEqualTo(DEFAULT_EGITIM_YERI);
        assertThat(testEgitim.getEgitimPuani()).isEqualTo(DEFAULT_EGITIM_PUANI);
        assertThat(testEgitim.getAktif()).isEqualTo(UPDATED_AKTIF);
    }

    @Test
    @Transactional
    void fullUpdateEgitimWithPatch() throws Exception {
        // Initialize the database
        egitimRepository.saveAndFlush(egitim);

        int databaseSizeBeforeUpdate = egitimRepository.findAll().size();

        // Update the egitim using partial update
        Egitim partialUpdatedEgitim = new Egitim();
        partialUpdatedEgitim.setId(egitim.getId());

        partialUpdatedEgitim
            .egitimBaslik(UPDATED_EGITIM_BASLIK)
            .egitimAltBaslik(UPDATED_EGITIM_ALT_BASLIK)
            .egitimBaslamaTarihi(UPDATED_EGITIM_BASLAMA_TARIHI)
            .egitimBitisTarihi(UPDATED_EGITIM_BITIS_TARIHI)
            .dersSayisi(UPDATED_DERS_SAYISI)
            .egitimSuresi(UPDATED_EGITIM_SURESI)
            .egitimYeri(UPDATED_EGITIM_YERI)
            .egitimPuani(UPDATED_EGITIM_PUANI)
            .aktif(UPDATED_AKTIF);

        restEgitimMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEgitim.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEgitim))
            )
            .andExpect(status().isOk());

        // Validate the Egitim in the database
        List<Egitim> egitimList = egitimRepository.findAll();
        assertThat(egitimList).hasSize(databaseSizeBeforeUpdate);
        Egitim testEgitim = egitimList.get(egitimList.size() - 1);
        assertThat(testEgitim.getEgitimBaslik()).isEqualTo(UPDATED_EGITIM_BASLIK);
        assertThat(testEgitim.getEgitimAltBaslik()).isEqualTo(UPDATED_EGITIM_ALT_BASLIK);
        assertThat(testEgitim.getEgitimBaslamaTarihi()).isEqualTo(UPDATED_EGITIM_BASLAMA_TARIHI);
        assertThat(testEgitim.getEgitimBitisTarihi()).isEqualTo(UPDATED_EGITIM_BITIS_TARIHI);
        assertThat(testEgitim.getDersSayisi()).isEqualTo(UPDATED_DERS_SAYISI);
        assertThat(testEgitim.getEgitimSuresi()).isEqualTo(UPDATED_EGITIM_SURESI);
        assertThat(testEgitim.getEgitimYeri()).isEqualTo(UPDATED_EGITIM_YERI);
        assertThat(testEgitim.getEgitimPuani()).isEqualTo(UPDATED_EGITIM_PUANI);
        assertThat(testEgitim.getAktif()).isEqualTo(UPDATED_AKTIF);
    }

    @Test
    @Transactional
    void patchNonExistingEgitim() throws Exception {
        int databaseSizeBeforeUpdate = egitimRepository.findAll().size();
        egitim.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEgitimMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, egitim.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(egitim))
            )
            .andExpect(status().isBadRequest());

        // Validate the Egitim in the database
        List<Egitim> egitimList = egitimRepository.findAll();
        assertThat(egitimList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEgitim() throws Exception {
        int databaseSizeBeforeUpdate = egitimRepository.findAll().size();
        egitim.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEgitimMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(egitim))
            )
            .andExpect(status().isBadRequest());

        // Validate the Egitim in the database
        List<Egitim> egitimList = egitimRepository.findAll();
        assertThat(egitimList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEgitim() throws Exception {
        int databaseSizeBeforeUpdate = egitimRepository.findAll().size();
        egitim.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEgitimMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(egitim)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Egitim in the database
        List<Egitim> egitimList = egitimRepository.findAll();
        assertThat(egitimList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEgitim() throws Exception {
        // Initialize the database
        egitimRepository.saveAndFlush(egitim);

        int databaseSizeBeforeDelete = egitimRepository.findAll().size();

        // Delete the egitim
        restEgitimMockMvc
            .perform(delete(ENTITY_API_URL_ID, egitim.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Egitim> egitimList = egitimRepository.findAll();
        assertThat(egitimList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
