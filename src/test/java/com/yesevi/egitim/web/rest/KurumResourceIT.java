package com.yesevi.egitim.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.yesevi.egitim.IntegrationTest;
import com.yesevi.egitim.domain.Kurum;
import com.yesevi.egitim.repository.KurumRepository;
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
 * Integration tests for the {@link KurumResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class KurumResourceIT {

    private static final String DEFAULT_ADI = "AAAAAAAAAA";
    private static final String UPDATED_ADI = "BBBBBBBBBB";

    private static final String DEFAULT_ACIKLAMA = "AAAAAAAAAA";
    private static final String UPDATED_ACIKLAMA = "BBBBBBBBBB";

    private static final Boolean DEFAULT_AKTIF = false;
    private static final Boolean UPDATED_AKTIF = true;

    private static final String ENTITY_API_URL = "/api/kurums";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private KurumRepository kurumRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restKurumMockMvc;

    private Kurum kurum;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Kurum createEntity(EntityManager em) {
        Kurum kurum = new Kurum().adi(DEFAULT_ADI).aciklama(DEFAULT_ACIKLAMA).aktif(DEFAULT_AKTIF);
        return kurum;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Kurum createUpdatedEntity(EntityManager em) {
        Kurum kurum = new Kurum().adi(UPDATED_ADI).aciklama(UPDATED_ACIKLAMA).aktif(UPDATED_AKTIF);
        return kurum;
    }

    @BeforeEach
    public void initTest() {
        kurum = createEntity(em);
    }

    @Test
    @Transactional
    void createKurum() throws Exception {
        int databaseSizeBeforeCreate = kurumRepository.findAll().size();
        // Create the Kurum
        restKurumMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(kurum)))
            .andExpect(status().isCreated());

        // Validate the Kurum in the database
        List<Kurum> kurumList = kurumRepository.findAll();
        assertThat(kurumList).hasSize(databaseSizeBeforeCreate + 1);
        Kurum testKurum = kurumList.get(kurumList.size() - 1);
        assertThat(testKurum.getAdi()).isEqualTo(DEFAULT_ADI);
        assertThat(testKurum.getAciklama()).isEqualTo(DEFAULT_ACIKLAMA);
        assertThat(testKurum.getAktif()).isEqualTo(DEFAULT_AKTIF);
    }

    @Test
    @Transactional
    void createKurumWithExistingId() throws Exception {
        // Create the Kurum with an existing ID
        kurum.setId(1L);

        int databaseSizeBeforeCreate = kurumRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restKurumMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(kurum)))
            .andExpect(status().isBadRequest());

        // Validate the Kurum in the database
        List<Kurum> kurumList = kurumRepository.findAll();
        assertThat(kurumList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllKurums() throws Exception {
        // Initialize the database
        kurumRepository.saveAndFlush(kurum);

        // Get all the kurumList
        restKurumMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(kurum.getId().intValue())))
            .andExpect(jsonPath("$.[*].adi").value(hasItem(DEFAULT_ADI)))
            .andExpect(jsonPath("$.[*].aciklama").value(hasItem(DEFAULT_ACIKLAMA)))
            .andExpect(jsonPath("$.[*].aktif").value(hasItem(DEFAULT_AKTIF.booleanValue())));
    }

    @Test
    @Transactional
    void getKurum() throws Exception {
        // Initialize the database
        kurumRepository.saveAndFlush(kurum);

        // Get the kurum
        restKurumMockMvc
            .perform(get(ENTITY_API_URL_ID, kurum.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(kurum.getId().intValue()))
            .andExpect(jsonPath("$.adi").value(DEFAULT_ADI))
            .andExpect(jsonPath("$.aciklama").value(DEFAULT_ACIKLAMA))
            .andExpect(jsonPath("$.aktif").value(DEFAULT_AKTIF.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingKurum() throws Exception {
        // Get the kurum
        restKurumMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewKurum() throws Exception {
        // Initialize the database
        kurumRepository.saveAndFlush(kurum);

        int databaseSizeBeforeUpdate = kurumRepository.findAll().size();

        // Update the kurum
        Kurum updatedKurum = kurumRepository.findById(kurum.getId()).get();
        // Disconnect from session so that the updates on updatedKurum are not directly saved in db
        em.detach(updatedKurum);
        updatedKurum.adi(UPDATED_ADI).aciklama(UPDATED_ACIKLAMA).aktif(UPDATED_AKTIF);

        restKurumMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedKurum.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedKurum))
            )
            .andExpect(status().isOk());

        // Validate the Kurum in the database
        List<Kurum> kurumList = kurumRepository.findAll();
        assertThat(kurumList).hasSize(databaseSizeBeforeUpdate);
        Kurum testKurum = kurumList.get(kurumList.size() - 1);
        assertThat(testKurum.getAdi()).isEqualTo(UPDATED_ADI);
        assertThat(testKurum.getAciklama()).isEqualTo(UPDATED_ACIKLAMA);
        assertThat(testKurum.getAktif()).isEqualTo(UPDATED_AKTIF);
    }

    @Test
    @Transactional
    void putNonExistingKurum() throws Exception {
        int databaseSizeBeforeUpdate = kurumRepository.findAll().size();
        kurum.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restKurumMockMvc
            .perform(
                put(ENTITY_API_URL_ID, kurum.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(kurum))
            )
            .andExpect(status().isBadRequest());

        // Validate the Kurum in the database
        List<Kurum> kurumList = kurumRepository.findAll();
        assertThat(kurumList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchKurum() throws Exception {
        int databaseSizeBeforeUpdate = kurumRepository.findAll().size();
        kurum.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restKurumMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(kurum))
            )
            .andExpect(status().isBadRequest());

        // Validate the Kurum in the database
        List<Kurum> kurumList = kurumRepository.findAll();
        assertThat(kurumList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamKurum() throws Exception {
        int databaseSizeBeforeUpdate = kurumRepository.findAll().size();
        kurum.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restKurumMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(kurum)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Kurum in the database
        List<Kurum> kurumList = kurumRepository.findAll();
        assertThat(kurumList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateKurumWithPatch() throws Exception {
        // Initialize the database
        kurumRepository.saveAndFlush(kurum);

        int databaseSizeBeforeUpdate = kurumRepository.findAll().size();

        // Update the kurum using partial update
        Kurum partialUpdatedKurum = new Kurum();
        partialUpdatedKurum.setId(kurum.getId());

        partialUpdatedKurum.adi(UPDATED_ADI).aktif(UPDATED_AKTIF);

        restKurumMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedKurum.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedKurum))
            )
            .andExpect(status().isOk());

        // Validate the Kurum in the database
        List<Kurum> kurumList = kurumRepository.findAll();
        assertThat(kurumList).hasSize(databaseSizeBeforeUpdate);
        Kurum testKurum = kurumList.get(kurumList.size() - 1);
        assertThat(testKurum.getAdi()).isEqualTo(UPDATED_ADI);
        assertThat(testKurum.getAciklama()).isEqualTo(DEFAULT_ACIKLAMA);
        assertThat(testKurum.getAktif()).isEqualTo(UPDATED_AKTIF);
    }

    @Test
    @Transactional
    void fullUpdateKurumWithPatch() throws Exception {
        // Initialize the database
        kurumRepository.saveAndFlush(kurum);

        int databaseSizeBeforeUpdate = kurumRepository.findAll().size();

        // Update the kurum using partial update
        Kurum partialUpdatedKurum = new Kurum();
        partialUpdatedKurum.setId(kurum.getId());

        partialUpdatedKurum.adi(UPDATED_ADI).aciklama(UPDATED_ACIKLAMA).aktif(UPDATED_AKTIF);

        restKurumMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedKurum.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedKurum))
            )
            .andExpect(status().isOk());

        // Validate the Kurum in the database
        List<Kurum> kurumList = kurumRepository.findAll();
        assertThat(kurumList).hasSize(databaseSizeBeforeUpdate);
        Kurum testKurum = kurumList.get(kurumList.size() - 1);
        assertThat(testKurum.getAdi()).isEqualTo(UPDATED_ADI);
        assertThat(testKurum.getAciklama()).isEqualTo(UPDATED_ACIKLAMA);
        assertThat(testKurum.getAktif()).isEqualTo(UPDATED_AKTIF);
    }

    @Test
    @Transactional
    void patchNonExistingKurum() throws Exception {
        int databaseSizeBeforeUpdate = kurumRepository.findAll().size();
        kurum.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restKurumMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, kurum.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(kurum))
            )
            .andExpect(status().isBadRequest());

        // Validate the Kurum in the database
        List<Kurum> kurumList = kurumRepository.findAll();
        assertThat(kurumList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchKurum() throws Exception {
        int databaseSizeBeforeUpdate = kurumRepository.findAll().size();
        kurum.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restKurumMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(kurum))
            )
            .andExpect(status().isBadRequest());

        // Validate the Kurum in the database
        List<Kurum> kurumList = kurumRepository.findAll();
        assertThat(kurumList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamKurum() throws Exception {
        int databaseSizeBeforeUpdate = kurumRepository.findAll().size();
        kurum.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restKurumMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(kurum)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Kurum in the database
        List<Kurum> kurumList = kurumRepository.findAll();
        assertThat(kurumList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteKurum() throws Exception {
        // Initialize the database
        kurumRepository.saveAndFlush(kurum);

        int databaseSizeBeforeDelete = kurumRepository.findAll().size();

        // Delete the kurum
        restKurumMockMvc
            .perform(delete(ENTITY_API_URL_ID, kurum.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Kurum> kurumList = kurumRepository.findAll();
        assertThat(kurumList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
