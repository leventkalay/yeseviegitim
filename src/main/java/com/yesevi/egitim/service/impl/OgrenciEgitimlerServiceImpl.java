package com.yesevi.egitim.service.impl;

import com.yesevi.egitim.domain.OgrenciEgitimler;
import com.yesevi.egitim.repository.OgrenciEgitimlerRepository;
import com.yesevi.egitim.service.OgrenciEgitimlerService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link OgrenciEgitimler}.
 */
@Service
@Transactional
public class OgrenciEgitimlerServiceImpl implements OgrenciEgitimlerService {

    private final Logger log = LoggerFactory.getLogger(OgrenciEgitimlerServiceImpl.class);

    private final OgrenciEgitimlerRepository ogrenciEgitimlerRepository;

    public OgrenciEgitimlerServiceImpl(OgrenciEgitimlerRepository ogrenciEgitimlerRepository) {
        this.ogrenciEgitimlerRepository = ogrenciEgitimlerRepository;
    }

    @Override
    public OgrenciEgitimler save(OgrenciEgitimler ogrenciEgitimler) {
        log.debug("Request to save OgrenciEgitimler : {}", ogrenciEgitimler);
        return ogrenciEgitimlerRepository.save(ogrenciEgitimler);
    }

    @Override
    public Optional<OgrenciEgitimler> partialUpdate(OgrenciEgitimler ogrenciEgitimler) {
        log.debug("Request to partially update OgrenciEgitimler : {}", ogrenciEgitimler);

        return ogrenciEgitimlerRepository
            .findById(ogrenciEgitimler.getId())
            .map(existingOgrenciEgitimler -> {
                return existingOgrenciEgitimler;
            })
            .map(ogrenciEgitimlerRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OgrenciEgitimler> findAll(Pageable pageable) {
        log.debug("Request to get all OgrenciEgitimlers");
        return ogrenciEgitimlerRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OgrenciEgitimler> findOne(Long id) {
        log.debug("Request to get OgrenciEgitimler : {}", id);
        return ogrenciEgitimlerRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete OgrenciEgitimler : {}", id);
        ogrenciEgitimlerRepository.deleteById(id);
    }
}
