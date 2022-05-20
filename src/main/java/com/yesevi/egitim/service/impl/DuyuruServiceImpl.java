package com.yesevi.egitim.service.impl;

import com.yesevi.egitim.domain.Duyuru;
import com.yesevi.egitim.repository.DuyuruRepository;
import com.yesevi.egitim.service.DuyuruService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Duyuru}.
 */
@Service
@Transactional
public class DuyuruServiceImpl implements DuyuruService {

    private final Logger log = LoggerFactory.getLogger(DuyuruServiceImpl.class);

    private final DuyuruRepository duyuruRepository;

    public DuyuruServiceImpl(DuyuruRepository duyuruRepository) {
        this.duyuruRepository = duyuruRepository;
    }

    @Override
    public Duyuru save(Duyuru duyuru) {
        log.debug("Request to save Duyuru : {}", duyuru);
        return duyuruRepository.save(duyuru);
    }

    @Override
    public Optional<Duyuru> partialUpdate(Duyuru duyuru) {
        log.debug("Request to partially update Duyuru : {}", duyuru);

        return duyuruRepository
            .findById(duyuru.getId())
            .map(existingDuyuru -> {
                if (duyuru.getDuyuruBaslik() != null) {
                    existingDuyuru.setDuyuruBaslik(duyuru.getDuyuruBaslik());
                }
                if (duyuru.getDuyuruIcerik() != null) {
                    existingDuyuru.setDuyuruIcerik(duyuru.getDuyuruIcerik());
                }
                if (duyuru.getDuyuruBaslamaTarihi() != null) {
                    existingDuyuru.setDuyuruBaslamaTarihi(duyuru.getDuyuruBaslamaTarihi());
                }
                if (duyuru.getDuyuruBitisTarihi() != null) {
                    existingDuyuru.setDuyuruBitisTarihi(duyuru.getDuyuruBitisTarihi());
                }

                return existingDuyuru;
            })
            .map(duyuruRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Duyuru> findAll(Pageable pageable) {
        log.debug("Request to get all Duyurus");
        return duyuruRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Duyuru> findOne(Long id) {
        log.debug("Request to get Duyuru : {}", id);
        return duyuruRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Duyuru : {}", id);
        duyuruRepository.deleteById(id);
    }
}
