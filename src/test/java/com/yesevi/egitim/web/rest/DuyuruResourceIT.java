package com.yesevi.egitim.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.yesevi.egitim.IntegrationTest;
import com.yesevi.egitim.domain.Duyuru;
import com.yesevi.egitim.repository.DuyuruRepository;
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
 * Integration tests for the {@link DuyuruResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DuyuruResourceIT {

    private static final String DEFAULT_DUYURU_BASLIK = "AAAAAAAAAA";
    private static final String UPDATED_DUYURU_BASLIK = "BBBBBBBBBB";

    private static final String DEFAULT_DUYURU_ICERIK = "AAAAAAAAAA";
    private static final String UPDATED_DUYURU_ICERIK = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DUYURU_BASLAMA_TARIHI = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DUYURU_BASLAMA_TARIHI = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DUYURU_BITIS_TARIHI = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DUYURU_BITIS_TARIHI = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/duyurus";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DuyuruRepository duyuruRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDuyuruMockMvc;

    private Duyuru duyuru;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Duyuru createEntity(EntityManager em) {
        Duyuru duyuru = new Duyuru()
            .duyuruBaslik(DEFAULT_DUYURU_BASLIK)
            .duyuruIcerik(DEFAULT_DUYURU_ICERIK)
            .duyuruBaslamaTarihi(DEFAULT_DUYURU_BASLAMA_TARIHI)
            .duyuruBitisTarihi(DEFAULT_DUYURU_BITIS_TARIHI);
        return duyuru;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Duyuru createUpdatedEntity(EntityManager em) {
        Duyuru duyuru = new Duyuru()
            .duyuruBaslik(UPDATED_DUYURU_BASLIK)
            .duyuruIcerik(UPDATED_DUYURU_ICERIK)
            .duyuruBaslamaTarihi(UPDATED_DUYURU_BASLAMA_TARIHI)
            .duyuruBitisTarihi(UPDATED_DUYURU_BITIS_TARIHI);
        return duyuru;
    }

    @BeforeEach
    public void initTest() {
        duyuru = createEntity(em);
    }

    @Test
    @Transactional
    void createDuyuru() throws Exception {
        int databaseSizeBeforeCreate = duyuruRepository.findAll().size();
        // Create the Duyuru
        restDuyuruMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(duyuru)))
            .andExpect(status().isCreated());

        // Validate the Duyuru in the database
        List<Duyuru> duyuruList = duyuruRepository.findAll();
        assertThat(duyuruList).hasSize(databaseSizeBeforeCreate + 1);
        Duyuru testDuyuru = duyuruList.get(duyuruList.size() - 1);
        assertThat(testDuyuru.getDuyuruBaslik()).isEqualTo(DEFAULT_DUYURU_BASLIK);
        assertThat(testDuyuru.getDuyuruIcerik()).isEqualTo(DEFAULT_DUYURU_ICERIK);
        assertThat(testDuyuru.getDuyuruBaslamaTarihi()).isEqualTo(DEFAULT_DUYURU_BASLAMA_TARIHI);
        assertThat(testDuyuru.getDuyuruBitisTarihi()).isEqualTo(DEFAULT_DUYURU_BITIS_TARIHI);
    }

    @Test
    @Transactional
    void createDuyuruWithExistingId() throws Exception {
        // Create the Duyuru with an existing ID
        duyuru.setId(1L);

        int databaseSizeBeforeCreate = duyuruRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDuyuruMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(duyuru)))
            .andExpect(status().isBadRequest());

        // Validate the Duyuru in the database
        List<Duyuru> duyuruList = duyuruRepository.findAll();
        assertThat(duyuruList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDuyuruBaslikIsRequired() throws Exception {
        int databaseSizeBeforeTest = duyuruRepository.findAll().size();
        // set the field null
        duyuru.setDuyuruBaslik(null);

        // Create the Duyuru, which fails.

        restDuyuruMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(duyuru)))
            .andExpect(status().isBadRequest());

        List<Duyuru> duyuruList = duyuruRepository.findAll();
        assertThat(duyuruList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDuyuruIcerikIsRequired() throws Exception {
        int databaseSizeBeforeTest = duyuruRepository.findAll().size();
        // set the field null
        duyuru.setDuyuruIcerik(null);

        // Create the Duyuru, which fails.

        restDuyuruMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(duyuru)))
            .andExpect(status().isBadRequest());

        List<Duyuru> duyuruList = duyuruRepository.findAll();
        assertThat(duyuruList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDuyuruBaslamaTarihiIsRequired() throws Exception {
        int databaseSizeBeforeTest = duyuruRepository.findAll().size();
        // set the field null
        duyuru.setDuyuruBaslamaTarihi(null);

        // Create the Duyuru, which fails.

        restDuyuruMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(duyuru)))
            .andExpect(status().isBadRequest());

        List<Duyuru> duyuruList = duyuruRepository.findAll();
        assertThat(duyuruList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDuyuruBitisTarihiIsRequired() throws Exception {
        int databaseSizeBeforeTest = duyuruRepository.findAll().size();
        // set the field null
        duyuru.setDuyuruBitisTarihi(null);

        // Create the Duyuru, which fails.

        restDuyuruMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(duyuru)))
            .andExpect(status().isBadRequest());

        List<Duyuru> duyuruList = duyuruRepository.findAll();
        assertThat(duyuruList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDuyurus() throws Exception {
        // Initialize the database
        duyuruRepository.saveAndFlush(duyuru);

        // Get all the duyuruList
        restDuyuruMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(duyuru.getId().intValue())))
            .andExpect(jsonPath("$.[*].duyuruBaslik").value(hasItem(DEFAULT_DUYURU_BASLIK)))
            .andExpect(jsonPath("$.[*].duyuruIcerik").value(hasItem(DEFAULT_DUYURU_ICERIK)))
            .andExpect(jsonPath("$.[*].duyuruBaslamaTarihi").value(hasItem(DEFAULT_DUYURU_BASLAMA_TARIHI.toString())))
            .andExpect(jsonPath("$.[*].duyuruBitisTarihi").value(hasItem(DEFAULT_DUYURU_BITIS_TARIHI.toString())));
    }

    @Test
    @Transactional
    void getDuyuru() throws Exception {
        // Initialize the database
        duyuruRepository.saveAndFlush(duyuru);

        // Get the duyuru
        restDuyuruMockMvc
            .perform(get(ENTITY_API_URL_ID, duyuru.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(duyuru.getId().intValue()))
            .andExpect(jsonPath("$.duyuruBaslik").value(DEFAULT_DUYURU_BASLIK))
            .andExpect(jsonPath("$.duyuruIcerik").value(DEFAULT_DUYURU_ICERIK))
            .andExpect(jsonPath("$.duyuruBaslamaTarihi").value(DEFAULT_DUYURU_BASLAMA_TARIHI.toString()))
            .andExpect(jsonPath("$.duyuruBitisTarihi").value(DEFAULT_DUYURU_BITIS_TARIHI.toString()));
    }

    @Test
    @Transactional
    void getNonExistingDuyuru() throws Exception {
        // Get the duyuru
        restDuyuruMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDuyuru() throws Exception {
        // Initialize the database
        duyuruRepository.saveAndFlush(duyuru);

        int databaseSizeBeforeUpdate = duyuruRepository.findAll().size();

        // Update the duyuru
        Duyuru updatedDuyuru = duyuruRepository.findById(duyuru.getId()).get();
        // Disconnect from session so that the updates on updatedDuyuru are not directly saved in db
        em.detach(updatedDuyuru);
        updatedDuyuru
            .duyuruBaslik(UPDATED_DUYURU_BASLIK)
            .duyuruIcerik(UPDATED_DUYURU_ICERIK)
            .duyuruBaslamaTarihi(UPDATED_DUYURU_BASLAMA_TARIHI)
            .duyuruBitisTarihi(UPDATED_DUYURU_BITIS_TARIHI);

        restDuyuruMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDuyuru.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDuyuru))
            )
            .andExpect(status().isOk());

        // Validate the Duyuru in the database
        List<Duyuru> duyuruList = duyuruRepository.findAll();
        assertThat(duyuruList).hasSize(databaseSizeBeforeUpdate);
        Duyuru testDuyuru = duyuruList.get(duyuruList.size() - 1);
        assertThat(testDuyuru.getDuyuruBaslik()).isEqualTo(UPDATED_DUYURU_BASLIK);
        assertThat(testDuyuru.getDuyuruIcerik()).isEqualTo(UPDATED_DUYURU_ICERIK);
        assertThat(testDuyuru.getDuyuruBaslamaTarihi()).isEqualTo(UPDATED_DUYURU_BASLAMA_TARIHI);
        assertThat(testDuyuru.getDuyuruBitisTarihi()).isEqualTo(UPDATED_DUYURU_BITIS_TARIHI);
    }

    @Test
    @Transactional
    void putNonExistingDuyuru() throws Exception {
        int databaseSizeBeforeUpdate = duyuruRepository.findAll().size();
        duyuru.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDuyuruMockMvc
            .perform(
                put(ENTITY_API_URL_ID, duyuru.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(duyuru))
            )
            .andExpect(status().isBadRequest());

        // Validate the Duyuru in the database
        List<Duyuru> duyuruList = duyuruRepository.findAll();
        assertThat(duyuruList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDuyuru() throws Exception {
        int databaseSizeBeforeUpdate = duyuruRepository.findAll().size();
        duyuru.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDuyuruMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(duyuru))
            )
            .andExpect(status().isBadRequest());

        // Validate the Duyuru in the database
        List<Duyuru> duyuruList = duyuruRepository.findAll();
        assertThat(duyuruList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDuyuru() throws Exception {
        int databaseSizeBeforeUpdate = duyuruRepository.findAll().size();
        duyuru.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDuyuruMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(duyuru)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Duyuru in the database
        List<Duyuru> duyuruList = duyuruRepository.findAll();
        assertThat(duyuruList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDuyuruWithPatch() throws Exception {
        // Initialize the database
        duyuruRepository.saveAndFlush(duyuru);

        int databaseSizeBeforeUpdate = duyuruRepository.findAll().size();

        // Update the duyuru using partial update
        Duyuru partialUpdatedDuyuru = new Duyuru();
        partialUpdatedDuyuru.setId(duyuru.getId());

        partialUpdatedDuyuru.duyuruIcerik(UPDATED_DUYURU_ICERIK).duyuruBaslamaTarihi(UPDATED_DUYURU_BASLAMA_TARIHI);

        restDuyuruMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDuyuru.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDuyuru))
            )
            .andExpect(status().isOk());

        // Validate the Duyuru in the database
        List<Duyuru> duyuruList = duyuruRepository.findAll();
        assertThat(duyuruList).hasSize(databaseSizeBeforeUpdate);
        Duyuru testDuyuru = duyuruList.get(duyuruList.size() - 1);
        assertThat(testDuyuru.getDuyuruBaslik()).isEqualTo(DEFAULT_DUYURU_BASLIK);
        assertThat(testDuyuru.getDuyuruIcerik()).isEqualTo(UPDATED_DUYURU_ICERIK);
        assertThat(testDuyuru.getDuyuruBaslamaTarihi()).isEqualTo(UPDATED_DUYURU_BASLAMA_TARIHI);
        assertThat(testDuyuru.getDuyuruBitisTarihi()).isEqualTo(DEFAULT_DUYURU_BITIS_TARIHI);
    }

    @Test
    @Transactional
    void fullUpdateDuyuruWithPatch() throws Exception {
        // Initialize the database
        duyuruRepository.saveAndFlush(duyuru);

        int databaseSizeBeforeUpdate = duyuruRepository.findAll().size();

        // Update the duyuru using partial update
        Duyuru partialUpdatedDuyuru = new Duyuru();
        partialUpdatedDuyuru.setId(duyuru.getId());

        partialUpdatedDuyuru
            .duyuruBaslik(UPDATED_DUYURU_BASLIK)
            .duyuruIcerik(UPDATED_DUYURU_ICERIK)
            .duyuruBaslamaTarihi(UPDATED_DUYURU_BASLAMA_TARIHI)
            .duyuruBitisTarihi(UPDATED_DUYURU_BITIS_TARIHI);

        restDuyuruMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDuyuru.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDuyuru))
            )
            .andExpect(status().isOk());

        // Validate the Duyuru in the database
        List<Duyuru> duyuruList = duyuruRepository.findAll();
        assertThat(duyuruList).hasSize(databaseSizeBeforeUpdate);
        Duyuru testDuyuru = duyuruList.get(duyuruList.size() - 1);
        assertThat(testDuyuru.getDuyuruBaslik()).isEqualTo(UPDATED_DUYURU_BASLIK);
        assertThat(testDuyuru.getDuyuruIcerik()).isEqualTo(UPDATED_DUYURU_ICERIK);
        assertThat(testDuyuru.getDuyuruBaslamaTarihi()).isEqualTo(UPDATED_DUYURU_BASLAMA_TARIHI);
        assertThat(testDuyuru.getDuyuruBitisTarihi()).isEqualTo(UPDATED_DUYURU_BITIS_TARIHI);
    }

    @Test
    @Transactional
    void patchNonExistingDuyuru() throws Exception {
        int databaseSizeBeforeUpdate = duyuruRepository.findAll().size();
        duyuru.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDuyuruMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, duyuru.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(duyuru))
            )
            .andExpect(status().isBadRequest());

        // Validate the Duyuru in the database
        List<Duyuru> duyuruList = duyuruRepository.findAll();
        assertThat(duyuruList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDuyuru() throws Exception {
        int databaseSizeBeforeUpdate = duyuruRepository.findAll().size();
        duyuru.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDuyuruMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(duyuru))
            )
            .andExpect(status().isBadRequest());

        // Validate the Duyuru in the database
        List<Duyuru> duyuruList = duyuruRepository.findAll();
        assertThat(duyuruList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDuyuru() throws Exception {
        int databaseSizeBeforeUpdate = duyuruRepository.findAll().size();
        duyuru.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDuyuruMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(duyuru)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Duyuru in the database
        List<Duyuru> duyuruList = duyuruRepository.findAll();
        assertThat(duyuruList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDuyuru() throws Exception {
        // Initialize the database
        duyuruRepository.saveAndFlush(duyuru);

        int databaseSizeBeforeDelete = duyuruRepository.findAll().size();

        // Delete the duyuru
        restDuyuruMockMvc
            .perform(delete(ENTITY_API_URL_ID, duyuru.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Duyuru> duyuruList = duyuruRepository.findAll();
        assertThat(duyuruList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
