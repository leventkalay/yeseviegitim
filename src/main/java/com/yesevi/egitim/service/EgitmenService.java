package com.yesevi.egitim.service;

import com.yesevi.egitim.domain.Egitmen;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Egitmen}.
 */
public interface EgitmenService {
    /**
     * Save a egitmen.
     *
     * @param egitmen the entity to save.
     * @return the persisted entity.
     */
    Egitmen save(Egitmen egitmen);

    /**
     * Partially updates a egitmen.
     *
     * @param egitmen the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Egitmen> partialUpdate(Egitmen egitmen);

    /**
     * Get all the egitmen.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Egitmen> findAll(Pageable pageable);

    /**
     * Get the "id" egitmen.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Egitmen> findOne(Long id);

    /**
     * Delete the "id" egitmen.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
