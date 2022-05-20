package com.yesevi.egitim.service;

import com.yesevi.egitim.domain.EgitimDersler;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link EgitimDersler}.
 */
public interface EgitimDerslerService {
    /**
     * Save a egitimDersler.
     *
     * @param egitimDersler the entity to save.
     * @return the persisted entity.
     */
    EgitimDersler save(EgitimDersler egitimDersler);

    /**
     * Partially updates a egitimDersler.
     *
     * @param egitimDersler the entity to update partially.
     * @return the persisted entity.
     */
    Optional<EgitimDersler> partialUpdate(EgitimDersler egitimDersler);

    /**
     * Get all the egitimDerslers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<EgitimDersler> findAll(Pageable pageable);

    /**
     * Get the "id" egitimDersler.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EgitimDersler> findOne(Long id);

    /**
     * Delete the "id" egitimDersler.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
