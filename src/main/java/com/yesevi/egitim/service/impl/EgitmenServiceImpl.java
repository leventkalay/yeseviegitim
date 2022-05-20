package com.yesevi.egitim.service.impl;

import com.yesevi.egitim.domain.Egitmen;
import com.yesevi.egitim.repository.EgitmenRepository;
import com.yesevi.egitim.service.EgitmenService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Egitmen}.
 */
@Service
@Transactional
public class EgitmenServiceImpl implements EgitmenService {

    private final Logger log = LoggerFactory.getLogger(EgitmenServiceImpl.class);

    private final EgitmenRepository egitmenRepository;

    public EgitmenServiceImpl(EgitmenRepository egitmenRepository) {
        this.egitmenRepository = egitmenRepository;
    }

    @Override
    public Egitmen save(Egitmen egitmen) {
        log.debug("Request to save Egitmen : {}", egitmen);
        return egitmenRepository.save(egitmen);
    }

    @Override
    public Optional<Egitmen> partialUpdate(Egitmen egitmen) {
        log.debug("Request to partially update Egitmen : {}", egitmen);

        return egitmenRepository
            .findById(egitmen.getId())
            .map(existingEgitmen -> {
                if (egitmen.getAdiSoyadi() != null) {
                    existingEgitmen.setAdiSoyadi(egitmen.getAdiSoyadi());
                }
                if (egitmen.getUnvani() != null) {
                    existingEgitmen.setUnvani(egitmen.getUnvani());
                }
                if (egitmen.getPuani() != null) {
                    existingEgitmen.setPuani(egitmen.getPuani());
                }
                if (egitmen.getAktif() != null) {
                    existingEgitmen.setAktif(egitmen.getAktif());
                }

                return existingEgitmen;
            })
            .map(egitmenRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Egitmen> findAll(Pageable pageable) {
        log.debug("Request to get all Egitmen");
        return egitmenRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Egitmen> findOne(Long id) {
        log.debug("Request to get Egitmen : {}", id);
        return egitmenRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Egitmen : {}", id);
        egitmenRepository.deleteById(id);
    }
}
