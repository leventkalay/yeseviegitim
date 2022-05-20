package com.yesevi.egitim.service;

import com.yesevi.egitim.domain.Takvim;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Takvim}.
 */
public interface TakvimService {
    /**
     * Save a takvim.
     *
     * @param takvim the entity to save.
     * @return the persisted entity.
     */
    Takvim save(Takvim takvim);

    /**
     * Partially updates a takvim.
     *
     * @param takvim the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Takvim> partialUpdate(Takvim takvim);

    /**
     * Get all the takvims.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Takvim> findAll(Pageable pageable);

    /**
     * Get the "id" takvim.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Takvim> findOne(Long id);

    /**
     * Delete the "id" takvim.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
