package com.yesevi.egitim.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.yesevi.egitim.IntegrationTest;
import com.yesevi.egitim.domain.Ders;
import com.yesevi.egitim.repository.DersRepository;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link DersResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DersResourceIT {

    private static final String DEFAULT_ADI = "AAAAAAAAAA";
    private static final String UPDATED_ADI = "BBBBBBBBBB";

    private static final String DEFAULT_ACIKLAMA = "AAAAAAAAAA";
    private static final String UPDATED_ACIKLAMA = "BBBBBBBBBB";

    private static final String DEFAULT_DERS_LINKI = "AAAAAAAAAA";
    private static final String UPDATED_DERS_LINKI = "BBBBBBBBBB";

    private static final byte[] DEFAULT_DERS_VIDEOSU = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_DERS_VIDEOSU = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_DERS_VIDEOSU_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_DERS_VIDEOSU_CONTENT_TYPE = "image/png";

    private static final String ENTITY_API_URL = "/api/ders";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DersRepository dersRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDersMockMvc;

    private Ders ders;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ders createEntity(EntityManager em) {
        Ders ders = new Ders()
            .adi(DEFAULT_ADI)
            .aciklama(DEFAULT_ACIKLAMA)
            .dersLinki(DEFAULT_DERS_LINKI)
            .dersVideosu(DEFAULT_DERS_VIDEOSU)
            .dersVideosuContentType(DEFAULT_DERS_VIDEOSU_CONTENT_TYPE);
        return ders;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ders createUpdatedEntity(EntityManager em) {
        Ders ders = new Ders()
            .adi(UPDATED_ADI)
            .aciklama(UPDATED_ACIKLAMA)
            .dersLinki(UPDATED_DERS_LINKI)
            .dersVideosu(UPDATED_DERS_VIDEOSU)
            .dersVideosuContentType(UPDATED_DERS_VIDEOSU_CONTENT_TYPE);
        return ders;
    }

    @BeforeEach
    public void initTest() {
        ders = createEntity(em);
    }

    @Test
    @Transactional
    void createDers() throws Exception {
        int databaseSizeBeforeCreate = dersRepository.findAll().size();
        // Create the Ders
        restDersMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ders)))
            .andExpect(status().isCreated());

        // Validate the Ders in the database
        List<Ders> dersList = dersRepository.findAll();
        assertThat(dersList).hasSize(databaseSizeBeforeCreate + 1);
        Ders testDers = dersList.get(dersList.size() - 1);
        assertThat(testDers.getAdi()).isEqualTo(DEFAULT_ADI);
        assertThat(testDers.getAciklama()).isEqualTo(DEFAULT_ACIKLAMA);
        assertThat(testDers.getDersLinki()).isEqualTo(DEFAULT_DERS_LINKI);
        assertThat(testDers.getDersVideosu()).isEqualTo(DEFAULT_DERS_VIDEOSU);
        assertThat(testDers.getDersVideosuContentType()).isEqualTo(DEFAULT_DERS_VIDEOSU_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void createDersWithExistingId() throws Exception {
        // Create the Ders with an existing ID
        ders.setId(1L);

        int databaseSizeBeforeCreate = dersRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDersMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ders)))
            .andExpect(status().isBadRequest());

        // Validate the Ders in the database
        List<Ders> dersList = dersRepository.findAll();
        assertThat(dersList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDers() throws Exception {
        // Initialize the database
        dersRepository.saveAndFlush(ders);

        // Get all the dersList
        restDersMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ders.getId().intValue())))
            .andExpect(jsonPath("$.[*].adi").value(hasItem(DEFAULT_ADI)))
            .andExpect(jsonPath("$.[*].aciklama").value(hasItem(DEFAULT_ACIKLAMA.toString())))
            .andExpect(jsonPath("$.[*].dersLinki").value(hasItem(DEFAULT_DERS_LINKI)))
            .andExpect(jsonPath("$.[*].dersVideosuContentType").value(hasItem(DEFAULT_DERS_VIDEOSU_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].dersVideosu").value(hasItem(Base64Utils.encodeToString(DEFAULT_DERS_VIDEOSU))));
    }

    @Test
    @Transactional
    void getDers() throws Exception {
        // Initialize the database
        dersRepository.saveAndFlush(ders);

        // Get the ders
        restDersMockMvc
            .perform(get(ENTITY_API_URL_ID, ders.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ders.getId().intValue()))
            .andExpect(jsonPath("$.adi").value(DEFAULT_ADI))
            .andExpect(jsonPath("$.aciklama").value(DEFAULT_ACIKLAMA.toString()))
            .andExpect(jsonPath("$.dersLinki").value(DEFAULT_DERS_LINKI))
            .andExpect(jsonPath("$.dersVideosuContentType").value(DEFAULT_DERS_VIDEOSU_CONTENT_TYPE))
            .andExpect(jsonPath("$.dersVideosu").value(Base64Utils.encodeToString(DEFAULT_DERS_VIDEOSU)));
    }

    @Test
    @Transactional
    void getNonExistingDers() throws Exception {
        // Get the ders
        restDersMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDers() throws Exception {
        // Initialize the database
        dersRepository.saveAndFlush(ders);

        int databaseSizeBeforeUpdate = dersRepository.findAll().size();

        // Update the ders
        Ders updatedDers = dersRepository.findById(ders.getId()).get();
        // Disconnect from session so that the updates on updatedDers are not directly saved in db
        em.detach(updatedDers);
        updatedDers
            .adi(UPDATED_ADI)
            .aciklama(UPDATED_ACIKLAMA)
            .dersLinki(UPDATED_DERS_LINKI)
            .dersVideosu(UPDATED_DERS_VIDEOSU)
            .dersVideosuContentType(UPDATED_DERS_VIDEOSU_CONTENT_TYPE);

        restDersMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDers.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDers))
            )
            .andExpect(status().isOk());

        // Validate the Ders in the database
        List<Ders> dersList = dersRepository.findAll();
        assertThat(dersList).hasSize(databaseSizeBeforeUpdate);
        Ders testDers = dersList.get(dersList.size() - 1);
        assertThat(testDers.getAdi()).isEqualTo(UPDATED_ADI);
        assertThat(testDers.getAciklama()).isEqualTo(UPDATED_ACIKLAMA);
        assertThat(testDers.getDersLinki()).isEqualTo(UPDATED_DERS_LINKI);
        assertThat(testDers.getDersVideosu()).isEqualTo(UPDATED_DERS_VIDEOSU);
        assertThat(testDers.getDersVideosuContentType()).isEqualTo(UPDATED_DERS_VIDEOSU_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingDers() throws Exception {
        int databaseSizeBeforeUpdate = dersRepository.findAll().size();
        ders.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDersMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ders.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ders))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ders in the database
        List<Ders> dersList = dersRepository.findAll();
        assertThat(dersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDers() throws Exception {
        int databaseSizeBeforeUpdate = dersRepository.findAll().size();
        ders.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDersMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ders))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ders in the database
        List<Ders> dersList = dersRepository.findAll();
        assertThat(dersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDers() throws Exception {
        int databaseSizeBeforeUpdate = dersRepository.findAll().size();
        ders.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDersMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ders)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Ders in the database
        List<Ders> dersList = dersRepository.findAll();
        assertThat(dersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDersWithPatch() throws Exception {
        // Initialize the database
        dersRepository.saveAndFlush(ders);

        int databaseSizeBeforeUpdate = dersRepository.findAll().size();

        // Update the ders using partial update
        Ders partialUpdatedDers = new Ders();
        partialUpdatedDers.setId(ders.getId());

        restDersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDers.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDers))
            )
            .andExpect(status().isOk());

        // Validate the Ders in the database
        List<Ders> dersList = dersRepository.findAll();
        assertThat(dersList).hasSize(databaseSizeBeforeUpdate);
        Ders testDers = dersList.get(dersList.size() - 1);
        assertThat(testDers.getAdi()).isEqualTo(DEFAULT_ADI);
        assertThat(testDers.getAciklama()).isEqualTo(DEFAULT_ACIKLAMA);
        assertThat(testDers.getDersLinki()).isEqualTo(DEFAULT_DERS_LINKI);
        assertThat(testDers.getDersVideosu()).isEqualTo(DEFAULT_DERS_VIDEOSU);
        assertThat(testDers.getDersVideosuContentType()).isEqualTo(DEFAULT_DERS_VIDEOSU_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateDersWithPatch() throws Exception {
        // Initialize the database
        dersRepository.saveAndFlush(ders);

        int databaseSizeBeforeUpdate = dersRepository.findAll().size();

        // Update the ders using partial update
        Ders partialUpdatedDers = new Ders();
        partialUpdatedDers.setId(ders.getId());

        partialUpdatedDers
            .adi(UPDATED_ADI)
            .aciklama(UPDATED_ACIKLAMA)
            .dersLinki(UPDATED_DERS_LINKI)
            .dersVideosu(UPDATED_DERS_VIDEOSU)
            .dersVideosuContentType(UPDATED_DERS_VIDEOSU_CONTENT_TYPE);

        restDersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDers.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDers))
            )
            .andExpect(status().isOk());

        // Validate the Ders in the database
        List<Ders> dersList = dersRepository.findAll();
        assertThat(dersList).hasSize(databaseSizeBeforeUpdate);
        Ders testDers = dersList.get(dersList.size() - 1);
        assertThat(testDers.getAdi()).isEqualTo(UPDATED_ADI);
        assertThat(testDers.getAciklama()).isEqualTo(UPDATED_ACIKLAMA);
        assertThat(testDers.getDersLinki()).isEqualTo(UPDATED_DERS_LINKI);
        assertThat(testDers.getDersVideosu()).isEqualTo(UPDATED_DERS_VIDEOSU);
        assertThat(testDers.getDersVideosuContentType()).isEqualTo(UPDATED_DERS_VIDEOSU_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingDers() throws Exception {
        int databaseSizeBeforeUpdate = dersRepository.findAll().size();
        ders.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ders.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ders))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ders in the database
        List<Ders> dersList = dersRepository.findAll();
        assertThat(dersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDers() throws Exception {
        int databaseSizeBeforeUpdate = dersRepository.findAll().size();
        ders.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ders))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ders in the database
        List<Ders> dersList = dersRepository.findAll();
        assertThat(dersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDers() throws Exception {
        int databaseSizeBeforeUpdate = dersRepository.findAll().size();
        ders.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDersMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(ders)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Ders in the database
        List<Ders> dersList = dersRepository.findAll();
        assertThat(dersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDers() throws Exception {
        // Initialize the database
        dersRepository.saveAndFlush(ders);

        int databaseSizeBeforeDelete = dersRepository.findAll().size();

        // Delete the ders
        restDersMockMvc
            .perform(delete(ENTITY_API_URL_ID, ders.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Ders> dersList = dersRepository.findAll();
        assertThat(dersList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
