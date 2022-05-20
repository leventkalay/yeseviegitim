package com.yesevi.egitim.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.yesevi.egitim.IntegrationTest;
import com.yesevi.egitim.domain.EgitimTuru;
import com.yesevi.egitim.repository.EgitimTuruRepository;
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
 * Integration tests for the {@link EgitimTuruResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EgitimTuruResourceIT {

    private static final String DEFAULT_ADI = "AAAAAAAAAA";
    private static final String UPDATED_ADI = "BBBBBBBBBB";

    private static final String DEFAULT_ACIKLAMA = "AAAAAAAAAA";
    private static final String UPDATED_ACIKLAMA = "BBBBBBBBBB";

    private static final Boolean DEFAULT_AKTIF = false;
    private static final Boolean UPDATED_AKTIF = true;

    private static final String ENTITY_API_URL = "/api/egitim-turus";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EgitimTuruRepository egitimTuruRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEgitimTuruMockMvc;

    private EgitimTuru egitimTuru;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EgitimTuru createEntity(EntityManager em) {
        EgitimTuru egitimTuru = new EgitimTuru().adi(DEFAULT_ADI).aciklama(DEFAULT_ACIKLAMA).aktif(DEFAULT_AKTIF);
        return egitimTuru;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EgitimTuru createUpdatedEntity(EntityManager em) {
        EgitimTuru egitimTuru = new EgitimTuru().adi(UPDATED_ADI).aciklama(UPDATED_ACIKLAMA).aktif(UPDATED_AKTIF);
        return egitimTuru;
    }

    @BeforeEach
    public void initTest() {
        egitimTuru = createEntity(em);
    }

    @Test
    @Transactional
    void createEgitimTuru() throws Exception {
        int databaseSizeBeforeCreate = egitimTuruRepository.findAll().size();
        // Create the EgitimTuru
        restEgitimTuruMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(egitimTuru)))
            .andExpect(status().isCreated());

        // Validate the EgitimTuru in the database
        List<EgitimTuru> egitimTuruList = egitimTuruRepository.findAll();
        assertThat(egitimTuruList).hasSize(databaseSizeBeforeCreate + 1);
        EgitimTuru testEgitimTuru = egitimTuruList.get(egitimTuruList.size() - 1);
        assertThat(testEgitimTuru.getAdi()).isEqualTo(DEFAULT_ADI);
        assertThat(testEgitimTuru.getAciklama()).isEqualTo(DEFAULT_ACIKLAMA);
        assertThat(testEgitimTuru.getAktif()).isEqualTo(DEFAULT_AKTIF);
    }

    @Test
    @Transactional
    void createEgitimTuruWithExistingId() throws Exception {
        // Create the EgitimTuru with an existing ID
        egitimTuru.setId(1L);

        int databaseSizeBeforeCreate = egitimTuruRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEgitimTuruMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(egitimTuru)))
            .andExpect(status().isBadRequest());

        // Validate the EgitimTuru in the database
        List<EgitimTuru> egitimTuruList = egitimTuruRepository.findAll();
        assertThat(egitimTuruList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllEgitimTurus() throws Exception {
        // Initialize the database
        egitimTuruRepository.saveAndFlush(egitimTuru);

        // Get all the egitimTuruList
        restEgitimTuruMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(egitimTuru.getId().intValue())))
            .andExpect(jsonPath("$.[*].adi").value(hasItem(DEFAULT_ADI)))
            .andExpect(jsonPath("$.[*].aciklama").value(hasItem(DEFAULT_ACIKLAMA)))
            .andExpect(jsonPath("$.[*].aktif").value(hasItem(DEFAULT_AKTIF.booleanValue())));
    }

    @Test
    @Transactional
    void getEgitimTuru() throws Exception {
        // Initialize the database
        egitimTuruRepository.saveAndFlush(egitimTuru);

        // Get the egitimTuru
        restEgitimTuruMockMvc
            .perform(get(ENTITY_API_URL_ID, egitimTuru.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(egitimTuru.getId().intValue()))
            .andExpect(jsonPath("$.adi").value(DEFAULT_ADI))
            .andExpect(jsonPath("$.aciklama").value(DEFAULT_ACIKLAMA))
            .andExpect(jsonPath("$.aktif").value(DEFAULT_AKTIF.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingEgitimTuru() throws Exception {
        // Get the egitimTuru
        restEgitimTuruMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewEgitimTuru() throws Exception {
        // Initialize the database
        egitimTuruRepository.saveAndFlush(egitimTuru);

        int databaseSizeBeforeUpdate = egitimTuruRepository.findAll().size();

        // Update the egitimTuru
        EgitimTuru updatedEgitimTuru = egitimTuruRepository.findById(egitimTuru.getId()).get();
        // Disconnect from session so that the updates on updatedEgitimTuru are not directly saved in db
        em.detach(updatedEgitimTuru);
        updatedEgitimTuru.adi(UPDATED_ADI).aciklama(UPDATED_ACIKLAMA).aktif(UPDATED_AKTIF);

        restEgitimTuruMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEgitimTuru.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedEgitimTuru))
            )
            .andExpect(status().isOk());

        // Validate the EgitimTuru in the database
        List<EgitimTuru> egitimTuruList = egitimTuruRepository.findAll();
        assertThat(egitimTuruList).hasSize(databaseSizeBeforeUpdate);
        EgitimTuru testEgitimTuru = egitimTuruList.get(egitimTuruList.size() - 1);
        assertThat(testEgitimTuru.getAdi()).isEqualTo(UPDATED_ADI);
        assertThat(testEgitimTuru.getAciklama()).isEqualTo(UPDATED_ACIKLAMA);
        assertThat(testEgitimTuru.getAktif()).isEqualTo(UPDATED_AKTIF);
    }

    @Test
    @Transactional
    void putNonExistingEgitimTuru() throws Exception {
        int databaseSizeBeforeUpdate = egitimTuruRepository.findAll().size();
        egitimTuru.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEgitimTuruMockMvc
            .perform(
                put(ENTITY_API_URL_ID, egitimTuru.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(egitimTuru))
            )
            .andExpect(status().isBadRequest());

        // Validate the EgitimTuru in the database
        List<EgitimTuru> egitimTuruList = egitimTuruRepository.findAll();
        assertThat(egitimTuruList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEgitimTuru() throws Exception {
        int databaseSizeBeforeUpdate = egitimTuruRepository.findAll().size();
        egitimTuru.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEgitimTuruMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(egitimTuru))
            )
            .andExpect(status().isBadRequest());

        // Validate the EgitimTuru in the database
        List<EgitimTuru> egitimTuruList = egitimTuruRepository.findAll();
        assertThat(egitimTuruList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEgitimTuru() throws Exception {
        int databaseSizeBeforeUpdate = egitimTuruRepository.findAll().size();
        egitimTuru.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEgitimTuruMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(egitimTuru)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the EgitimTuru in the database
        List<EgitimTuru> egitimTuruList = egitimTuruRepository.findAll();
        assertThat(egitimTuruList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEgitimTuruWithPatch() throws Exception {
        // Initialize the database
        egitimTuruRepository.saveAndFlush(egitimTuru);

        int databaseSizeBeforeUpdate = egitimTuruRepository.findAll().size();

        // Update the egitimTuru using partial update
        EgitimTuru partialUpdatedEgitimTuru = new EgitimTuru();
        partialUpdatedEgitimTuru.setId(egitimTuru.getId());

        partialUpdatedEgitimTuru.aktif(UPDATED_AKTIF);

        restEgitimTuruMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEgitimTuru.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEgitimTuru))
            )
            .andExpect(status().isOk());

        // Validate the EgitimTuru in the database
        List<EgitimTuru> egitimTuruList = egitimTuruRepository.findAll();
        assertThat(egitimTuruList).hasSize(databaseSizeBeforeUpdate);
        EgitimTuru testEgitimTuru = egitimTuruList.get(egitimTuruList.size() - 1);
        assertThat(testEgitimTuru.getAdi()).isEqualTo(DEFAULT_ADI);
        assertThat(testEgitimTuru.getAciklama()).isEqualTo(DEFAULT_ACIKLAMA);
        assertThat(testEgitimTuru.getAktif()).isEqualTo(UPDATED_AKTIF);
    }

    @Test
    @Transactional
    void fullUpdateEgitimTuruWithPatch() throws Exception {
        // Initialize the database
        egitimTuruRepository.saveAndFlush(egitimTuru);

        int databaseSizeBeforeUpdate = egitimTuruRepository.findAll().size();

        // Update the egitimTuru using partial update
        EgitimTuru partialUpdatedEgitimTuru = new EgitimTuru();
        partialUpdatedEgitimTuru.setId(egitimTuru.getId());

        partialUpdatedEgitimTuru.adi(UPDATED_ADI).aciklama(UPDATED_ACIKLAMA).aktif(UPDATED_AKTIF);

        restEgitimTuruMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEgitimTuru.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEgitimTuru))
            )
            .andExpect(status().isOk());

        // Validate the EgitimTuru in the database
        List<EgitimTuru> egitimTuruList = egitimTuruRepository.findAll();
        assertThat(egitimTuruList).hasSize(databaseSizeBeforeUpdate);
        EgitimTuru testEgitimTuru = egitimTuruList.get(egitimTuruList.size() - 1);
        assertThat(testEgitimTuru.getAdi()).isEqualTo(UPDATED_ADI);
        assertThat(testEgitimTuru.getAciklama()).isEqualTo(UPDATED_ACIKLAMA);
        assertThat(testEgitimTuru.getAktif()).isEqualTo(UPDATED_AKTIF);
    }

    @Test
    @Transactional
    void patchNonExistingEgitimTuru() throws Exception {
        int databaseSizeBeforeUpdate = egitimTuruRepository.findAll().size();
        egitimTuru.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEgitimTuruMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, egitimTuru.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(egitimTuru))
            )
            .andExpect(status().isBadRequest());

        // Validate the EgitimTuru in the database
        List<EgitimTuru> egitimTuruList = egitimTuruRepository.findAll();
        assertThat(egitimTuruList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEgitimTuru() throws Exception {
        int databaseSizeBeforeUpdate = egitimTuruRepository.findAll().size();
        egitimTuru.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEgitimTuruMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(egitimTuru))
            )
            .andExpect(status().isBadRequest());

        // Validate the EgitimTuru in the database
        List<EgitimTuru> egitimTuruList = egitimTuruRepository.findAll();
        assertThat(egitimTuruList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEgitimTuru() throws Exception {
        int databaseSizeBeforeUpdate = egitimTuruRepository.findAll().size();
        egitimTuru.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEgitimTuruMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(egitimTuru))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EgitimTuru in the database
        List<EgitimTuru> egitimTuruList = egitimTuruRepository.findAll();
        assertThat(egitimTuruList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEgitimTuru() throws Exception {
        // Initialize the database
        egitimTuruRepository.saveAndFlush(egitimTuru);

        int databaseSizeBeforeDelete = egitimTuruRepository.findAll().size();

        // Delete the egitimTuru
        restEgitimTuruMockMvc
            .perform(delete(ENTITY_API_URL_ID, egitimTuru.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<EgitimTuru> egitimTuruList = egitimTuruRepository.findAll();
        assertThat(egitimTuruList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
