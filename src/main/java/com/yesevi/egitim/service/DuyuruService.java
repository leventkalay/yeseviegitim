package com.yesevi.egitim.service;

import com.yesevi.egitim.domain.Duyuru;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Duyuru}.
 */
public interface DuyuruService {
    /**
     * Save a duyuru.
     *
     * @param duyuru the entity to save.
     * @return the persisted entity.
     */
    Duyuru save(Duyuru duyuru);

    /**
     * Partially updates a duyuru.
     *
     * @param duyuru the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Duyuru> partialUpdate(Duyuru duyuru);

    /**
     * Get all the duyurus.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Duyuru> findAll(Pageable pageable);

    /**
     * Get the "id" duyuru.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Duyuru> findOne(Long id);

    /**
     * Delete the "id" duyuru.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
