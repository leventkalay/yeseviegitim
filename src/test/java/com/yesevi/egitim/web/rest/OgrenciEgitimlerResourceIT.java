package com.yesevi.egitim.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.yesevi.egitim.IntegrationTest;
import com.yesevi.egitim.domain.OgrenciEgitimler;
import com.yesevi.egitim.repository.OgrenciEgitimlerRepository;
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
 * Integration tests for the {@link OgrenciEgitimlerResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OgrenciEgitimlerResourceIT {

    private static final String ENTITY_API_URL = "/api/ogrenci-egitimlers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OgrenciEgitimlerRepository ogrenciEgitimlerRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOgrenciEgitimlerMockMvc;

    private OgrenciEgitimler ogrenciEgitimler;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OgrenciEgitimler createEntity(EntityManager em) {
        OgrenciEgitimler ogrenciEgitimler = new OgrenciEgitimler();
        return ogrenciEgitimler;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OgrenciEgitimler createUpdatedEntity(EntityManager em) {
        OgrenciEgitimler ogrenciEgitimler = new OgrenciEgitimler();
        return ogrenciEgitimler;
    }

    @BeforeEach
    public void initTest() {
        ogrenciEgitimler = createEntity(em);
    }

    @Test
    @Transactional
    void createOgrenciEgitimler() throws Exception {
        int databaseSizeBeforeCreate = ogrenciEgitimlerRepository.findAll().size();
        // Create the OgrenciEgitimler
        restOgrenciEgitimlerMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ogrenciEgitimler))
            )
            .andExpect(status().isCreated());

        // Validate the OgrenciEgitimler in the database
        List<OgrenciEgitimler> ogrenciEgitimlerList = ogrenciEgitimlerRepository.findAll();
        assertThat(ogrenciEgitimlerList).hasSize(databaseSizeBeforeCreate + 1);
        OgrenciEgitimler testOgrenciEgitimler = ogrenciEgitimlerList.get(ogrenciEgitimlerList.size() - 1);
    }

    @Test
    @Transactional
    void createOgrenciEgitimlerWithExistingId() throws Exception {
        // Create the OgrenciEgitimler with an existing ID
        ogrenciEgitimler.setId(1L);

        int databaseSizeBeforeCreate = ogrenciEgitimlerRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOgrenciEgitimlerMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ogrenciEgitimler))
            )
            .andExpect(status().isBadRequest());

        // Validate the OgrenciEgitimler in the database
        List<OgrenciEgitimler> ogrenciEgitimlerList = ogrenciEgitimlerRepository.findAll();
        assertThat(ogrenciEgitimlerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllOgrenciEgitimlers() throws Exception {
        // Initialize the database
        ogrenciEgitimlerRepository.saveAndFlush(ogrenciEgitimler);

        // Get all the ogrenciEgitimlerList
        restOgrenciEgitimlerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ogrenciEgitimler.getId().intValue())));
    }

    @Test
    @Transactional
    void getOgrenciEgitimler() throws Exception {
        // Initialize the database
        ogrenciEgitimlerRepository.saveAndFlush(ogrenciEgitimler);

        // Get the ogrenciEgitimler
        restOgrenciEgitimlerMockMvc
            .perform(get(ENTITY_API_URL_ID, ogrenciEgitimler.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ogrenciEgitimler.getId().intValue()));
    }

    @Test
    @Transactional
    void getNonExistingOgrenciEgitimler() throws Exception {
        // Get the ogrenciEgitimler
        restOgrenciEgitimlerMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewOgrenciEgitimler() throws Exception {
        // Initialize the database
        ogrenciEgitimlerRepository.saveAndFlush(ogrenciEgitimler);

        int databaseSizeBeforeUpdate = ogrenciEgitimlerRepository.findAll().size();

        // Update the ogrenciEgitimler
        OgrenciEgitimler updatedOgrenciEgitimler = ogrenciEgitimlerRepository.findById(ogrenciEgitimler.getId()).get();
        // Disconnect from session so that the updates on updatedOgrenciEgitimler are not directly saved in db
        em.detach(updatedOgrenciEgitimler);

        restOgrenciEgitimlerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOgrenciEgitimler.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedOgrenciEgitimler))
            )
            .andExpect(status().isOk());

        // Validate the OgrenciEgitimler in the database
        List<OgrenciEgitimler> ogrenciEgitimlerList = ogrenciEgitimlerRepository.findAll();
        assertThat(ogrenciEgitimlerList).hasSize(databaseSizeBeforeUpdate);
        OgrenciEgitimler testOgrenciEgitimler = ogrenciEgitimlerList.get(ogrenciEgitimlerList.size() - 1);
    }

    @Test
    @Transactional
    void putNonExistingOgrenciEgitimler() throws Exception {
        int databaseSizeBeforeUpdate = ogrenciEgitimlerRepository.findAll().size();
        ogrenciEgitimler.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOgrenciEgitimlerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ogrenciEgitimler.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ogrenciEgitimler))
            )
            .andExpect(status().isBadRequest());

        // Validate the OgrenciEgitimler in the database
        List<OgrenciEgitimler> ogrenciEgitimlerList = ogrenciEgitimlerRepository.findAll();
        assertThat(ogrenciEgitimlerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOgrenciEgitimler() throws Exception {
        int databaseSizeBeforeUpdate = ogrenciEgitimlerRepository.findAll().size();
        ogrenciEgitimler.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOgrenciEgitimlerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ogrenciEgitimler))
            )
            .andExpect(status().isBadRequest());

        // Validate the OgrenciEgitimler in the database
        List<OgrenciEgitimler> ogrenciEgitimlerList = ogrenciEgitimlerRepository.findAll();
        assertThat(ogrenciEgitimlerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOgrenciEgitimler() throws Exception {
        int databaseSizeBeforeUpdate = ogrenciEgitimlerRepository.findAll().size();
        ogrenciEgitimler.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOgrenciEgitimlerMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ogrenciEgitimler))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OgrenciEgitimler in the database
        List<OgrenciEgitimler> ogrenciEgitimlerList = ogrenciEgitimlerRepository.findAll();
        assertThat(ogrenciEgitimlerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOgrenciEgitimlerWithPatch() throws Exception {
        // Initialize the database
        ogrenciEgitimlerRepository.saveAndFlush(ogrenciEgitimler);

        int databaseSizeBeforeUpdate = ogrenciEgitimlerRepository.findAll().size();

        // Update the ogrenciEgitimler using partial update
        OgrenciEgitimler partialUpdatedOgrenciEgitimler = new OgrenciEgitimler();
        partialUpdatedOgrenciEgitimler.setId(ogrenciEgitimler.getId());

        restOgrenciEgitimlerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOgrenciEgitimler.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOgrenciEgitimler))
            )
            .andExpect(status().isOk());

        // Validate the OgrenciEgitimler in the database
        List<OgrenciEgitimler> ogrenciEgitimlerList = ogrenciEgitimlerRepository.findAll();
        assertThat(ogrenciEgitimlerList).hasSize(databaseSizeBeforeUpdate);
        OgrenciEgitimler testOgrenciEgitimler = ogrenciEgitimlerList.get(ogrenciEgitimlerList.size() - 1);
    }

    @Test
    @Transactional
    void fullUpdateOgrenciEgitimlerWithPatch() throws Exception {
        // Initialize the database
        ogrenciEgitimlerRepository.saveAndFlush(ogrenciEgitimler);

        int databaseSizeBeforeUpdate = ogrenciEgitimlerRepository.findAll().size();

        // Update the ogrenciEgitimler using partial update
        OgrenciEgitimler partialUpdatedOgrenciEgitimler = new OgrenciEgitimler();
        partialUpdatedOgrenciEgitimler.setId(ogrenciEgitimler.getId());

        restOgrenciEgitimlerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOgrenciEgitimler.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOgrenciEgitimler))
            )
            .andExpect(status().isOk());

        // Validate the OgrenciEgitimler in the database
        List<OgrenciEgitimler> ogrenciEgitimlerList = ogrenciEgitimlerRepository.findAll();
        assertThat(ogrenciEgitimlerList).hasSize(databaseSizeBeforeUpdate);
        OgrenciEgitimler testOgrenciEgitimler = ogrenciEgitimlerList.get(ogrenciEgitimlerList.size() - 1);
    }

    @Test
    @Transactional
    void patchNonExistingOgrenciEgitimler() throws Exception {
        int databaseSizeBeforeUpdate = ogrenciEgitimlerRepository.findAll().size();
        ogrenciEgitimler.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOgrenciEgitimlerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ogrenciEgitimler.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ogrenciEgitimler))
            )
            .andExpect(status().isBadRequest());

        // Validate the OgrenciEgitimler in the database
        List<OgrenciEgitimler> ogrenciEgitimlerList = ogrenciEgitimlerRepository.findAll();
        assertThat(ogrenciEgitimlerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOgrenciEgitimler() throws Exception {
        int databaseSizeBeforeUpdate = ogrenciEgitimlerRepository.findAll().size();
        ogrenciEgitimler.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOgrenciEgitimlerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ogrenciEgitimler))
            )
            .andExpect(status().isBadRequest());

        // Validate the OgrenciEgitimler in the database
        List<OgrenciEgitimler> ogrenciEgitimlerList = ogrenciEgitimlerRepository.findAll();
        assertThat(ogrenciEgitimlerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOgrenciEgitimler() throws Exception {
        int databaseSizeBeforeUpdate = ogrenciEgitimlerRepository.findAll().size();
        ogrenciEgitimler.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOgrenciEgitimlerMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ogrenciEgitimler))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OgrenciEgitimler in the database
        List<OgrenciEgitimler> ogrenciEgitimlerList = ogrenciEgitimlerRepository.findAll();
        assertThat(ogrenciEgitimlerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOgrenciEgitimler() throws Exception {
        // Initialize the database
        ogrenciEgitimlerRepository.saveAndFlush(ogrenciEgitimler);

        int databaseSizeBeforeDelete = ogrenciEgitimlerRepository.findAll().size();

        // Delete the ogrenciEgitimler
        restOgrenciEgitimlerMockMvc
            .perform(delete(ENTITY_API_URL_ID, ogrenciEgitimler.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<OgrenciEgitimler> ogrenciEgitimlerList = ogrenciEgitimlerRepository.findAll();
        assertThat(ogrenciEgitimlerList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
