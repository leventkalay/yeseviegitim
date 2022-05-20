package com.yesevi.egitim.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.yesevi.egitim.IntegrationTest;
import com.yesevi.egitim.domain.EgitimDersler;
import com.yesevi.egitim.repository.EgitimDerslerRepository;
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
 * Integration tests for the {@link EgitimDerslerResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EgitimDerslerResourceIT {

    private static final String ENTITY_API_URL = "/api/egitim-derslers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EgitimDerslerRepository egitimDerslerRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEgitimDerslerMockMvc;

    private EgitimDersler egitimDersler;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EgitimDersler createEntity(EntityManager em) {
        EgitimDersler egitimDersler = new EgitimDersler();
        return egitimDersler;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EgitimDersler createUpdatedEntity(EntityManager em) {
        EgitimDersler egitimDersler = new EgitimDersler();
        return egitimDersler;
    }

    @BeforeEach
    public void initTest() {
        egitimDersler = createEntity(em);
    }

    @Test
    @Transactional
    void createEgitimDersler() throws Exception {
        int databaseSizeBeforeCreate = egitimDerslerRepository.findAll().size();
        // Create the EgitimDersler
        restEgitimDerslerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(egitimDersler)))
            .andExpect(status().isCreated());

        // Validate the EgitimDersler in the database
        List<EgitimDersler> egitimDerslerList = egitimDerslerRepository.findAll();
        assertThat(egitimDerslerList).hasSize(databaseSizeBeforeCreate + 1);
        EgitimDersler testEgitimDersler = egitimDerslerList.get(egitimDerslerList.size() - 1);
    }

    @Test
    @Transactional
    void createEgitimDerslerWithExistingId() throws Exception {
        // Create the EgitimDersler with an existing ID
        egitimDersler.setId(1L);

        int databaseSizeBeforeCreate = egitimDerslerRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEgitimDerslerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(egitimDersler)))
            .andExpect(status().isBadRequest());

        // Validate the EgitimDersler in the database
        List<EgitimDersler> egitimDerslerList = egitimDerslerRepository.findAll();
        assertThat(egitimDerslerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllEgitimDerslers() throws Exception {
        // Initialize the database
        egitimDerslerRepository.saveAndFlush(egitimDersler);

        // Get all the egitimDerslerList
        restEgitimDerslerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(egitimDersler.getId().intValue())));
    }

    @Test
    @Transactional
    void getEgitimDersler() throws Exception {
        // Initialize the database
        egitimDerslerRepository.saveAndFlush(egitimDersler);

        // Get the egitimDersler
        restEgitimDerslerMockMvc
            .perform(get(ENTITY_API_URL_ID, egitimDersler.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(egitimDersler.getId().intValue()));
    }

    @Test
    @Transactional
    void getNonExistingEgitimDersler() throws Exception {
        // Get the egitimDersler
        restEgitimDerslerMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewEgitimDersler() throws Exception {
        // Initialize the database
        egitimDerslerRepository.saveAndFlush(egitimDersler);

        int databaseSizeBeforeUpdate = egitimDerslerRepository.findAll().size();

        // Update the egitimDersler
        EgitimDersler updatedEgitimDersler = egitimDerslerRepository.findById(egitimDersler.getId()).get();
        // Disconnect from session so that the updates on updatedEgitimDersler are not directly saved in db
        em.detach(updatedEgitimDersler);

        restEgitimDerslerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEgitimDersler.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedEgitimDersler))
            )
            .andExpect(status().isOk());

        // Validate the EgitimDersler in the database
        List<EgitimDersler> egitimDerslerList = egitimDerslerRepository.findAll();
        assertThat(egitimDerslerList).hasSize(databaseSizeBeforeUpdate);
        EgitimDersler testEgitimDersler = egitimDerslerList.get(egitimDerslerList.size() - 1);
    }

    @Test
    @Transactional
    void putNonExistingEgitimDersler() throws Exception {
        int databaseSizeBeforeUpdate = egitimDerslerRepository.findAll().size();
        egitimDersler.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEgitimDerslerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, egitimDersler.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(egitimDersler))
            )
            .andExpect(status().isBadRequest());

        // Validate the EgitimDersler in the database
        List<EgitimDersler> egitimDerslerList = egitimDerslerRepository.findAll();
        assertThat(egitimDerslerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEgitimDersler() throws Exception {
        int databaseSizeBeforeUpdate = egitimDerslerRepository.findAll().size();
        egitimDersler.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEgitimDerslerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(egitimDersler))
            )
            .andExpect(status().isBadRequest());

        // Validate the EgitimDersler in the database
        List<EgitimDersler> egitimDerslerList = egitimDerslerRepository.findAll();
        assertThat(egitimDerslerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEgitimDersler() throws Exception {
        int databaseSizeBeforeUpdate = egitimDerslerRepository.findAll().size();
        egitimDersler.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEgitimDerslerMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(egitimDersler)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the EgitimDersler in the database
        List<EgitimDersler> egitimDerslerList = egitimDerslerRepository.findAll();
        assertThat(egitimDerslerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEgitimDerslerWithPatch() throws Exception {
        // Initialize the database
        egitimDerslerRepository.saveAndFlush(egitimDersler);

        int databaseSizeBeforeUpdate = egitimDerslerRepository.findAll().size();

        // Update the egitimDersler using partial update
        EgitimDersler partialUpdatedEgitimDersler = new EgitimDersler();
        partialUpdatedEgitimDersler.setId(egitimDersler.getId());

        restEgitimDerslerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEgitimDersler.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEgitimDersler))
            )
            .andExpect(status().isOk());

        // Validate the EgitimDersler in the database
        List<EgitimDersler> egitimDerslerList = egitimDerslerRepository.findAll();
        assertThat(egitimDerslerList).hasSize(databaseSizeBeforeUpdate);
        EgitimDersler testEgitimDersler = egitimDerslerList.get(egitimDerslerList.size() - 1);
    }

    @Test
    @Transactional
    void fullUpdateEgitimDerslerWithPatch() throws Exception {
        // Initialize the database
        egitimDerslerRepository.saveAndFlush(egitimDersler);

        int databaseSizeBeforeUpdate = egitimDerslerRepository.findAll().size();

        // Update the egitimDersler using partial update
        EgitimDersler partialUpdatedEgitimDersler = new EgitimDersler();
        partialUpdatedEgitimDersler.setId(egitimDersler.getId());

        restEgitimDerslerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEgitimDersler.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEgitimDersler))
            )
            .andExpect(status().isOk());

        // Validate the EgitimDersler in the database
        List<EgitimDersler> egitimDerslerList = egitimDerslerRepository.findAll();
        assertThat(egitimDerslerList).hasSize(databaseSizeBeforeUpdate);
        EgitimDersler testEgitimDersler = egitimDerslerList.get(egitimDerslerList.size() - 1);
    }

    @Test
    @Transactional
    void patchNonExistingEgitimDersler() throws Exception {
        int databaseSizeBeforeUpdate = egitimDerslerRepository.findAll().size();
        egitimDersler.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEgitimDerslerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, egitimDersler.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(egitimDersler))
            )
            .andExpect(status().isBadRequest());

        // Validate the EgitimDersler in the database
        List<EgitimDersler> egitimDerslerList = egitimDerslerRepository.findAll();
        assertThat(egitimDerslerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEgitimDersler() throws Exception {
        int databaseSizeBeforeUpdate = egitimDerslerRepository.findAll().size();
        egitimDersler.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEgitimDerslerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(egitimDersler))
            )
            .andExpect(status().isBadRequest());

        // Validate the EgitimDersler in the database
        List<EgitimDersler> egitimDerslerList = egitimDerslerRepository.findAll();
        assertThat(egitimDerslerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEgitimDersler() throws Exception {
        int databaseSizeBeforeUpdate = egitimDerslerRepository.findAll().size();
        egitimDersler.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEgitimDerslerMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(egitimDersler))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EgitimDersler in the database
        List<EgitimDersler> egitimDerslerList = egitimDerslerRepository.findAll();
        assertThat(egitimDerslerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEgitimDersler() throws Exception {
        // Initialize the database
        egitimDerslerRepository.saveAndFlush(egitimDersler);

        int databaseSizeBeforeDelete = egitimDerslerRepository.findAll().size();

        // Delete the egitimDersler
        restEgitimDerslerMockMvc
            .perform(delete(ENTITY_API_URL_ID, egitimDersler.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<EgitimDersler> egitimDerslerList = egitimDerslerRepository.findAll();
        assertThat(egitimDerslerList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
