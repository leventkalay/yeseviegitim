package com.yesevi.egitim.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.yesevi.egitim.IntegrationTest;
import com.yesevi.egitim.domain.Takvim;
import com.yesevi.egitim.repository.TakvimRepository;
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
 * Integration tests for the {@link TakvimResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TakvimResourceIT {

    private static final String DEFAULT_ADI = "AAAAAAAAAA";
    private static final String UPDATED_ADI = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/takvims";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TakvimRepository takvimRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTakvimMockMvc;

    private Takvim takvim;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Takvim createEntity(EntityManager em) {
        Takvim takvim = new Takvim().adi(DEFAULT_ADI);
        return takvim;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Takvim createUpdatedEntity(EntityManager em) {
        Takvim takvim = new Takvim().adi(UPDATED_ADI);
        return takvim;
    }

    @BeforeEach
    public void initTest() {
        takvim = createEntity(em);
    }

    @Test
    @Transactional
    void createTakvim() throws Exception {
        int databaseSizeBeforeCreate = takvimRepository.findAll().size();
        // Create the Takvim
        restTakvimMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(takvim)))
            .andExpect(status().isCreated());

        // Validate the Takvim in the database
        List<Takvim> takvimList = takvimRepository.findAll();
        assertThat(takvimList).hasSize(databaseSizeBeforeCreate + 1);
        Takvim testTakvim = takvimList.get(takvimList.size() - 1);
        assertThat(testTakvim.getAdi()).isEqualTo(DEFAULT_ADI);
    }

    @Test
    @Transactional
    void createTakvimWithExistingId() throws Exception {
        // Create the Takvim with an existing ID
        takvim.setId(1L);

        int databaseSizeBeforeCreate = takvimRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTakvimMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(takvim)))
            .andExpect(status().isBadRequest());

        // Validate the Takvim in the database
        List<Takvim> takvimList = takvimRepository.findAll();
        assertThat(takvimList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTakvims() throws Exception {
        // Initialize the database
        takvimRepository.saveAndFlush(takvim);

        // Get all the takvimList
        restTakvimMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(takvim.getId().intValue())))
            .andExpect(jsonPath("$.[*].adi").value(hasItem(DEFAULT_ADI)));
    }

    @Test
    @Transactional
    void getTakvim() throws Exception {
        // Initialize the database
        takvimRepository.saveAndFlush(takvim);

        // Get the takvim
        restTakvimMockMvc
            .perform(get(ENTITY_API_URL_ID, takvim.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(takvim.getId().intValue()))
            .andExpect(jsonPath("$.adi").value(DEFAULT_ADI));
    }

    @Test
    @Transactional
    void getNonExistingTakvim() throws Exception {
        // Get the takvim
        restTakvimMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTakvim() throws Exception {
        // Initialize the database
        takvimRepository.saveAndFlush(takvim);

        int databaseSizeBeforeUpdate = takvimRepository.findAll().size();

        // Update the takvim
        Takvim updatedTakvim = takvimRepository.findById(takvim.getId()).get();
        // Disconnect from session so that the updates on updatedTakvim are not directly saved in db
        em.detach(updatedTakvim);
        updatedTakvim.adi(UPDATED_ADI);

        restTakvimMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTakvim.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTakvim))
            )
            .andExpect(status().isOk());

        // Validate the Takvim in the database
        List<Takvim> takvimList = takvimRepository.findAll();
        assertThat(takvimList).hasSize(databaseSizeBeforeUpdate);
        Takvim testTakvim = takvimList.get(takvimList.size() - 1);
        assertThat(testTakvim.getAdi()).isEqualTo(UPDATED_ADI);
    }

    @Test
    @Transactional
    void putNonExistingTakvim() throws Exception {
        int databaseSizeBeforeUpdate = takvimRepository.findAll().size();
        takvim.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTakvimMockMvc
            .perform(
                put(ENTITY_API_URL_ID, takvim.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(takvim))
            )
            .andExpect(status().isBadRequest());

        // Validate the Takvim in the database
        List<Takvim> takvimList = takvimRepository.findAll();
        assertThat(takvimList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTakvim() throws Exception {
        int databaseSizeBeforeUpdate = takvimRepository.findAll().size();
        takvim.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTakvimMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(takvim))
            )
            .andExpect(status().isBadRequest());

        // Validate the Takvim in the database
        List<Takvim> takvimList = takvimRepository.findAll();
        assertThat(takvimList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTakvim() throws Exception {
        int databaseSizeBeforeUpdate = takvimRepository.findAll().size();
        takvim.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTakvimMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(takvim)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Takvim in the database
        List<Takvim> takvimList = takvimRepository.findAll();
        assertThat(takvimList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTakvimWithPatch() throws Exception {
        // Initialize the database
        takvimRepository.saveAndFlush(takvim);

        int databaseSizeBeforeUpdate = takvimRepository.findAll().size();

        // Update the takvim using partial update
        Takvim partialUpdatedTakvim = new Takvim();
        partialUpdatedTakvim.setId(takvim.getId());

        restTakvimMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTakvim.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTakvim))
            )
            .andExpect(status().isOk());

        // Validate the Takvim in the database
        List<Takvim> takvimList = takvimRepository.findAll();
        assertThat(takvimList).hasSize(databaseSizeBeforeUpdate);
        Takvim testTakvim = takvimList.get(takvimList.size() - 1);
        assertThat(testTakvim.getAdi()).isEqualTo(DEFAULT_ADI);
    }

    @Test
    @Transactional
    void fullUpdateTakvimWithPatch() throws Exception {
        // Initialize the database
        takvimRepository.saveAndFlush(takvim);

        int databaseSizeBeforeUpdate = takvimRepository.findAll().size();

        // Update the takvim using partial update
        Takvim partialUpdatedTakvim = new Takvim();
        partialUpdatedTakvim.setId(takvim.getId());

        partialUpdatedTakvim.adi(UPDATED_ADI);

        restTakvimMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTakvim.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTakvim))
            )
            .andExpect(status().isOk());

        // Validate the Takvim in the database
        List<Takvim> takvimList = takvimRepository.findAll();
        assertThat(takvimList).hasSize(databaseSizeBeforeUpdate);
        Takvim testTakvim = takvimList.get(takvimList.size() - 1);
        assertThat(testTakvim.getAdi()).isEqualTo(UPDATED_ADI);
    }

    @Test
    @Transactional
    void patchNonExistingTakvim() throws Exception {
        int databaseSizeBeforeUpdate = takvimRepository.findAll().size();
        takvim.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTakvimMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, takvim.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(takvim))
            )
            .andExpect(status().isBadRequest());

        // Validate the Takvim in the database
        List<Takvim> takvimList = takvimRepository.findAll();
        assertThat(takvimList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTakvim() throws Exception {
        int databaseSizeBeforeUpdate = takvimRepository.findAll().size();
        takvim.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTakvimMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(takvim))
            )
            .andExpect(status().isBadRequest());

        // Validate the Takvim in the database
        List<Takvim> takvimList = takvimRepository.findAll();
        assertThat(takvimList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTakvim() throws Exception {
        int databaseSizeBeforeUpdate = takvimRepository.findAll().size();
        takvim.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTakvimMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(takvim)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Takvim in the database
        List<Takvim> takvimList = takvimRepository.findAll();
        assertThat(takvimList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTakvim() throws Exception {
        // Initialize the database
        takvimRepository.saveAndFlush(takvim);

        int databaseSizeBeforeDelete = takvimRepository.findAll().size();

        // Delete the takvim
        restTakvimMockMvc
            .perform(delete(ENTITY_API_URL_ID, takvim.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Takvim> takvimList = takvimRepository.findAll();
        assertThat(takvimList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
