package com.yesevi.egitim.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.yesevi.egitim.IntegrationTest;
import com.yesevi.egitim.domain.Egitmen;
import com.yesevi.egitim.repository.EgitmenRepository;
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
 * Integration tests for the {@link EgitmenResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EgitmenResourceIT {

    private static final String DEFAULT_ADI_SOYADI = "AAAAAAAAAA";
    private static final String UPDATED_ADI_SOYADI = "BBBBBBBBBB";

    private static final String DEFAULT_UNVANI = "AAAAAAAAAA";
    private static final String UPDATED_UNVANI = "BBBBBBBBBB";

    private static final Float DEFAULT_PUANI = 1F;
    private static final Float UPDATED_PUANI = 2F;

    private static final Boolean DEFAULT_AKTIF = false;
    private static final Boolean UPDATED_AKTIF = true;

    private static final String ENTITY_API_URL = "/api/egitmen";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EgitmenRepository egitmenRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEgitmenMockMvc;

    private Egitmen egitmen;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Egitmen createEntity(EntityManager em) {
        Egitmen egitmen = new Egitmen().adiSoyadi(DEFAULT_ADI_SOYADI).unvani(DEFAULT_UNVANI).puani(DEFAULT_PUANI).aktif(DEFAULT_AKTIF);
        return egitmen;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Egitmen createUpdatedEntity(EntityManager em) {
        Egitmen egitmen = new Egitmen().adiSoyadi(UPDATED_ADI_SOYADI).unvani(UPDATED_UNVANI).puani(UPDATED_PUANI).aktif(UPDATED_AKTIF);
        return egitmen;
    }

    @BeforeEach
    public void initTest() {
        egitmen = createEntity(em);
    }

    @Test
    @Transactional
    void createEgitmen() throws Exception {
        int databaseSizeBeforeCreate = egitmenRepository.findAll().size();
        // Create the Egitmen
        restEgitmenMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(egitmen)))
            .andExpect(status().isCreated());

        // Validate the Egitmen in the database
        List<Egitmen> egitmenList = egitmenRepository.findAll();
        assertThat(egitmenList).hasSize(databaseSizeBeforeCreate + 1);
        Egitmen testEgitmen = egitmenList.get(egitmenList.size() - 1);
        assertThat(testEgitmen.getAdiSoyadi()).isEqualTo(DEFAULT_ADI_SOYADI);
        assertThat(testEgitmen.getUnvani()).isEqualTo(DEFAULT_UNVANI);
        assertThat(testEgitmen.getPuani()).isEqualTo(DEFAULT_PUANI);
        assertThat(testEgitmen.getAktif()).isEqualTo(DEFAULT_AKTIF);
    }

    @Test
    @Transactional
    void createEgitmenWithExistingId() throws Exception {
        // Create the Egitmen with an existing ID
        egitmen.setId(1L);

        int databaseSizeBeforeCreate = egitmenRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEgitmenMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(egitmen)))
            .andExpect(status().isBadRequest());

        // Validate the Egitmen in the database
        List<Egitmen> egitmenList = egitmenRepository.findAll();
        assertThat(egitmenList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllEgitmen() throws Exception {
        // Initialize the database
        egitmenRepository.saveAndFlush(egitmen);

        // Get all the egitmenList
        restEgitmenMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(egitmen.getId().intValue())))
            .andExpect(jsonPath("$.[*].adiSoyadi").value(hasItem(DEFAULT_ADI_SOYADI)))
            .andExpect(jsonPath("$.[*].unvani").value(hasItem(DEFAULT_UNVANI)))
            .andExpect(jsonPath("$.[*].puani").value(hasItem(DEFAULT_PUANI.doubleValue())))
            .andExpect(jsonPath("$.[*].aktif").value(hasItem(DEFAULT_AKTIF.booleanValue())));
    }

    @Test
    @Transactional
    void getEgitmen() throws Exception {
        // Initialize the database
        egitmenRepository.saveAndFlush(egitmen);

        // Get the egitmen
        restEgitmenMockMvc
            .perform(get(ENTITY_API_URL_ID, egitmen.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(egitmen.getId().intValue()))
            .andExpect(jsonPath("$.adiSoyadi").value(DEFAULT_ADI_SOYADI))
            .andExpect(jsonPath("$.unvani").value(DEFAULT_UNVANI))
            .andExpect(jsonPath("$.puani").value(DEFAULT_PUANI.doubleValue()))
            .andExpect(jsonPath("$.aktif").value(DEFAULT_AKTIF.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingEgitmen() throws Exception {
        // Get the egitmen
        restEgitmenMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewEgitmen() throws Exception {
        // Initialize the database
        egitmenRepository.saveAndFlush(egitmen);

        int databaseSizeBeforeUpdate = egitmenRepository.findAll().size();

        // Update the egitmen
        Egitmen updatedEgitmen = egitmenRepository.findById(egitmen.getId()).get();
        // Disconnect from session so that the updates on updatedEgitmen are not directly saved in db
        em.detach(updatedEgitmen);
        updatedEgitmen.adiSoyadi(UPDATED_ADI_SOYADI).unvani(UPDATED_UNVANI).puani(UPDATED_PUANI).aktif(UPDATED_AKTIF);

        restEgitmenMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEgitmen.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedEgitmen))
            )
            .andExpect(status().isOk());

        // Validate the Egitmen in the database
        List<Egitmen> egitmenList = egitmenRepository.findAll();
        assertThat(egitmenList).hasSize(databaseSizeBeforeUpdate);
        Egitmen testEgitmen = egitmenList.get(egitmenList.size() - 1);
        assertThat(testEgitmen.getAdiSoyadi()).isEqualTo(UPDATED_ADI_SOYADI);
        assertThat(testEgitmen.getUnvani()).isEqualTo(UPDATED_UNVANI);
        assertThat(testEgitmen.getPuani()).isEqualTo(UPDATED_PUANI);
        assertThat(testEgitmen.getAktif()).isEqualTo(UPDATED_AKTIF);
    }

    @Test
    @Transactional
    void putNonExistingEgitmen() throws Exception {
        int databaseSizeBeforeUpdate = egitmenRepository.findAll().size();
        egitmen.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEgitmenMockMvc
            .perform(
                put(ENTITY_API_URL_ID, egitmen.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(egitmen))
            )
            .andExpect(status().isBadRequest());

        // Validate the Egitmen in the database
        List<Egitmen> egitmenList = egitmenRepository.findAll();
        assertThat(egitmenList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEgitmen() throws Exception {
        int databaseSizeBeforeUpdate = egitmenRepository.findAll().size();
        egitmen.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEgitmenMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(egitmen))
            )
            .andExpect(status().isBadRequest());

        // Validate the Egitmen in the database
        List<Egitmen> egitmenList = egitmenRepository.findAll();
        assertThat(egitmenList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEgitmen() throws Exception {
        int databaseSizeBeforeUpdate = egitmenRepository.findAll().size();
        egitmen.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEgitmenMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(egitmen)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Egitmen in the database
        List<Egitmen> egitmenList = egitmenRepository.findAll();
        assertThat(egitmenList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEgitmenWithPatch() throws Exception {
        // Initialize the database
        egitmenRepository.saveAndFlush(egitmen);

        int databaseSizeBeforeUpdate = egitmenRepository.findAll().size();

        // Update the egitmen using partial update
        Egitmen partialUpdatedEgitmen = new Egitmen();
        partialUpdatedEgitmen.setId(egitmen.getId());

        partialUpdatedEgitmen.adiSoyadi(UPDATED_ADI_SOYADI).unvani(UPDATED_UNVANI);

        restEgitmenMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEgitmen.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEgitmen))
            )
            .andExpect(status().isOk());

        // Validate the Egitmen in the database
        List<Egitmen> egitmenList = egitmenRepository.findAll();
        assertThat(egitmenList).hasSize(databaseSizeBeforeUpdate);
        Egitmen testEgitmen = egitmenList.get(egitmenList.size() - 1);
        assertThat(testEgitmen.getAdiSoyadi()).isEqualTo(UPDATED_ADI_SOYADI);
        assertThat(testEgitmen.getUnvani()).isEqualTo(UPDATED_UNVANI);
        assertThat(testEgitmen.getPuani()).isEqualTo(DEFAULT_PUANI);
        assertThat(testEgitmen.getAktif()).isEqualTo(DEFAULT_AKTIF);
    }

    @Test
    @Transactional
    void fullUpdateEgitmenWithPatch() throws Exception {
        // Initialize the database
        egitmenRepository.saveAndFlush(egitmen);

        int databaseSizeBeforeUpdate = egitmenRepository.findAll().size();

        // Update the egitmen using partial update
        Egitmen partialUpdatedEgitmen = new Egitmen();
        partialUpdatedEgitmen.setId(egitmen.getId());

        partialUpdatedEgitmen.adiSoyadi(UPDATED_ADI_SOYADI).unvani(UPDATED_UNVANI).puani(UPDATED_PUANI).aktif(UPDATED_AKTIF);

        restEgitmenMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEgitmen.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEgitmen))
            )
            .andExpect(status().isOk());

        // Validate the Egitmen in the database
        List<Egitmen> egitmenList = egitmenRepository.findAll();
        assertThat(egitmenList).hasSize(databaseSizeBeforeUpdate);
        Egitmen testEgitmen = egitmenList.get(egitmenList.size() - 1);
        assertThat(testEgitmen.getAdiSoyadi()).isEqualTo(UPDATED_ADI_SOYADI);
        assertThat(testEgitmen.getUnvani()).isEqualTo(UPDATED_UNVANI);
        assertThat(testEgitmen.getPuani()).isEqualTo(UPDATED_PUANI);
        assertThat(testEgitmen.getAktif()).isEqualTo(UPDATED_AKTIF);
    }

    @Test
    @Transactional
    void patchNonExistingEgitmen() throws Exception {
        int databaseSizeBeforeUpdate = egitmenRepository.findAll().size();
        egitmen.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEgitmenMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, egitmen.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(egitmen))
            )
            .andExpect(status().isBadRequest());

        // Validate the Egitmen in the database
        List<Egitmen> egitmenList = egitmenRepository.findAll();
        assertThat(egitmenList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEgitmen() throws Exception {
        int databaseSizeBeforeUpdate = egitmenRepository.findAll().size();
        egitmen.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEgitmenMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(egitmen))
            )
            .andExpect(status().isBadRequest());

        // Validate the Egitmen in the database
        List<Egitmen> egitmenList = egitmenRepository.findAll();
        assertThat(egitmenList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEgitmen() throws Exception {
        int databaseSizeBeforeUpdate = egitmenRepository.findAll().size();
        egitmen.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEgitmenMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(egitmen)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Egitmen in the database
        List<Egitmen> egitmenList = egitmenRepository.findAll();
        assertThat(egitmenList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEgitmen() throws Exception {
        // Initialize the database
        egitmenRepository.saveAndFlush(egitmen);

        int databaseSizeBeforeDelete = egitmenRepository.findAll().size();

        // Delete the egitmen
        restEgitmenMockMvc
            .perform(delete(ENTITY_API_URL_ID, egitmen.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Egitmen> egitmenList = egitmenRepository.findAll();
        assertThat(egitmenList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
