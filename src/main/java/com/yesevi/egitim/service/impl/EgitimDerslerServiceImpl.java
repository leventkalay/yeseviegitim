package com.yesevi.egitim.service.impl;

import com.yesevi.egitim.domain.EgitimDersler;
import com.yesevi.egitim.repository.EgitimDerslerRepository;
import com.yesevi.egitim.service.EgitimDerslerService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link EgitimDersler}.
 */
@Service
@Transactional
public class EgitimDerslerServiceImpl implements EgitimDerslerService {

    private final Logger log = LoggerFactory.getLogger(EgitimDerslerServiceImpl.class);

    private final EgitimDerslerRepository egitimDerslerRepository;

    public EgitimDerslerServiceImpl(EgitimDerslerRepository egitimDerslerRepository) {
        this.egitimDerslerRepository = egitimDerslerRepository;
    }

    @Override
    public EgitimDersler save(EgitimDersler egitimDersler) {
        log.debug("Request to save EgitimDersler : {}", egitimDersler);
        return egitimDerslerRepository.save(egitimDersler);
    }

    @Override
    public Optional<EgitimDersler> partialUpdate(EgitimDersler egitimDersler) {
        log.debug("Request to partially update EgitimDersler : {}", egitimDersler);

        return egitimDerslerRepository
            .findById(egitimDersler.getId())
            .map(existingEgitimDersler -> {
                return existingEgitimDersler;
            })
            .map(egitimDerslerRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EgitimDersler> findAll(Pageable pageable) {
        log.debug("Request to get all EgitimDerslers");
        return egitimDerslerRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EgitimDersler> findOne(Long id) {
        log.debug("Request to get EgitimDersler : {}", id);
        return egitimDerslerRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete EgitimDersler : {}", id);
        egitimDerslerRepository.deleteById(id);
    }
}
