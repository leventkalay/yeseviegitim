package com.yesevi.egitim.service;

import com.yesevi.egitim.domain.Kurum;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Kurum}.
 */
public interface KurumService {
    /**
     * Save a kurum.
     *
     * @param kurum the entity to save.
     * @return the persisted entity.
     */
    Kurum save(Kurum kurum);

    /**
     * Partially updates a kurum.
     *
     * @param kurum the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Kurum> partialUpdate(Kurum kurum);

    /**
     * Get all the kurums.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Kurum> findAll(Pageable pageable);

    /**
     * Get the "id" kurum.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Kurum> findOne(Long id);

    /**
     * Delete the "id" kurum.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
