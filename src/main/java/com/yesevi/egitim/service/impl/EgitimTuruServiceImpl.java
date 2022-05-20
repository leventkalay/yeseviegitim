package com.yesevi.egitim.service.impl;

import com.yesevi.egitim.domain.EgitimTuru;
import com.yesevi.egitim.repository.EgitimTuruRepository;
import com.yesevi.egitim.service.EgitimTuruService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link EgitimTuru}.
 */
@Service
@Transactional
public class EgitimTuruServiceImpl implements EgitimTuruService {

    private final Logger log = LoggerFactory.getLogger(EgitimTuruServiceImpl.class);

    private final EgitimTuruRepository egitimTuruRepository;

    public EgitimTuruServiceImpl(EgitimTuruRepository egitimTuruRepository) {
        this.egitimTuruRepository = egitimTuruRepository;
    }

    @Override
    public EgitimTuru save(EgitimTuru egitimTuru) {
        log.debug("Request to save EgitimTuru : {}", egitimTuru);
        return egitimTuruRepository.save(egitimTuru);
    }

    @Override
    public Optional<EgitimTuru> partialUpdate(EgitimTuru egitimTuru) {
        log.debug("Request to partially update EgitimTuru : {}", egitimTuru);

        return egitimTuruRepository
            .findById(egitimTuru.getId())
            .map(existingEgitimTuru -> {
                if (egitimTuru.getAdi() != null) {
                    existingEgitimTuru.setAdi(egitimTuru.getAdi());
                }
                if (egitimTuru.getAciklama() != null) {
                    existingEgitimTuru.setAciklama(egitimTuru.getAciklama());
                }
                if (egitimTuru.getAktif() != null) {
                    existingEgitimTuru.setAktif(egitimTuru.getAktif());
                }

                return existingEgitimTuru;
            })
            .map(egitimTuruRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EgitimTuru> findAll(Pageable pageable) {
        log.debug("Request to get all EgitimTurus");
        return egitimTuruRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EgitimTuru> findOne(Long id) {
        log.debug("Request to get EgitimTuru : {}", id);
        return egitimTuruRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete EgitimTuru : {}", id);
        egitimTuruRepository.deleteById(id);
    }
}
