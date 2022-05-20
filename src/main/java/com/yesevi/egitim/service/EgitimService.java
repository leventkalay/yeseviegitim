package com.yesevi.egitim.service;

import com.yesevi.egitim.domain.Egitim;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Egitim}.
 */
public interface EgitimService {
    /**
     * Save a egitim.
     *
     * @param egitim the entity to save.
     * @return the persisted entity.
     */
    Egitim save(Egitim egitim);

    /**
     * Partially updates a egitim.
     *
     * @param egitim the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Egitim> partialUpdate(Egitim egitim);

    /**
     * Get all the egitims.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Egitim> findAll(Pageable pageable);

    /**
     * Get the "id" egitim.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Egitim> findOne(Long id);

    /**
     * Delete the "id" egitim.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
