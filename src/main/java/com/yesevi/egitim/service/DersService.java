package com.yesevi.egitim.service;

import com.yesevi.egitim.domain.Ders;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Ders}.
 */
public interface DersService {
    /**
     * Save a ders.
     *
     * @param ders the entity to save.
     * @return the persisted entity.
     */
    Ders save(Ders ders);

    /**
     * Partially updates a ders.
     *
     * @param ders the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Ders> partialUpdate(Ders ders);

    /**
     * Get all the ders.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Ders> findAll(Pageable pageable);

    /**
     * Get the "id" ders.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Ders> findOne(Long id);

    /**
     * Delete the "id" ders.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
