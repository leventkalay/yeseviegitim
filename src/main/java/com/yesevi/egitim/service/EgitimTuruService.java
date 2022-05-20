package com.yesevi.egitim.service;

import com.yesevi.egitim.domain.EgitimTuru;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link EgitimTuru}.
 */
public interface EgitimTuruService {
    /**
     * Save a egitimTuru.
     *
     * @param egitimTuru the entity to save.
     * @return the persisted entity.
     */
    EgitimTuru save(EgitimTuru egitimTuru);

    /**
     * Partially updates a egitimTuru.
     *
     * @param egitimTuru the entity to update partially.
     * @return the persisted entity.
     */
    Optional<EgitimTuru> partialUpdate(EgitimTuru egitimTuru);

    /**
     * Get all the egitimTurus.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<EgitimTuru> findAll(Pageable pageable);

    /**
     * Get the "id" egitimTuru.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EgitimTuru> findOne(Long id);

    /**
     * Delete the "id" egitimTuru.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
