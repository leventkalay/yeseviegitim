package com.yesevi.egitim.service;

import com.yesevi.egitim.domain.OgrenciEgitimler;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link OgrenciEgitimler}.
 */
public interface OgrenciEgitimlerService {
    /**
     * Save a ogrenciEgitimler.
     *
     * @param ogrenciEgitimler the entity to save.
     * @return the persisted entity.
     */
    OgrenciEgitimler save(OgrenciEgitimler ogrenciEgitimler);

    /**
     * Partially updates a ogrenciEgitimler.
     *
     * @param ogrenciEgitimler the entity to update partially.
     * @return the persisted entity.
     */
    Optional<OgrenciEgitimler> partialUpdate(OgrenciEgitimler ogrenciEgitimler);

    /**
     * Get all the ogrenciEgitimlers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<OgrenciEgitimler> findAll(Pageable pageable);

    /**
     * Get the "id" ogrenciEgitimler.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OgrenciEgitimler> findOne(Long id);

    /**
     * Delete the "id" ogrenciEgitimler.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
