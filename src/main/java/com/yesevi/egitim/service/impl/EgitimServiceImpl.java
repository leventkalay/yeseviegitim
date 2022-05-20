package com.yesevi.egitim.service.impl;

import com.yesevi.egitim.domain.Egitim;
import com.yesevi.egitim.repository.EgitimRepository;
import com.yesevi.egitim.service.EgitimService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Egitim}.
 */
@Service
@Transactional
public class EgitimServiceImpl implements EgitimService {

    private final Logger log = LoggerFactory.getLogger(EgitimServiceImpl.class);

    private final EgitimRepository egitimRepository;

    public EgitimServiceImpl(EgitimRepository egitimRepository) {
        this.egitimRepository = egitimRepository;
    }

    @Override
    public Egitim save(Egitim egitim) {
        log.debug("Request to save Egitim : {}", egitim);
        return egitimRepository.save(egitim);
    }

    @Override
    public Optional<Egitim> partialUpdate(Egitim egitim) {
        log.debug("Request to partially update Egitim : {}", egitim);

        return egitimRepository
            .findById(egitim.getId())
            .map(existingEgitim -> {
                if (egitim.getEgitimBaslik() != null) {
                    existingEgitim.setEgitimBaslik(egitim.getEgitimBaslik());
                }
                if (egitim.getEgitimAltBaslik() != null) {
                    existingEgitim.setEgitimAltBaslik(egitim.getEgitimAltBaslik());
                }
                if (egitim.getEgitimBaslamaTarihi() != null) {
                    existingEgitim.setEgitimBaslamaTarihi(egitim.getEgitimBaslamaTarihi());
                }
                if (egitim.getEgitimBitisTarihi() != null) {
                    existingEgitim.setEgitimBitisTarihi(egitim.getEgitimBitisTarihi());
                }
                if (egitim.getDersSayisi() != null) {
                    existingEgitim.setDersSayisi(egitim.getDersSayisi());
                }
                if (egitim.getEgitimSuresi() != null) {
                    existingEgitim.setEgitimSuresi(egitim.getEgitimSuresi());
                }
                if (egitim.getEgitimYeri() != null) {
                    existingEgitim.setEgitimYeri(egitim.getEgitimYeri());
                }
                if (egitim.getEgitimPuani() != null) {
                    existingEgitim.setEgitimPuani(egitim.getEgitimPuani());
                }
                if (egitim.getAktif() != null) {
                    existingEgitim.setAktif(egitim.getAktif());
                }

                return existingEgitim;
            })
            .map(egitimRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Egitim> findAll(Pageable pageable) {
        log.debug("Request to get all Egitims");
        return egitimRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Egitim> findOne(Long id) {
        log.debug("Request to get Egitim : {}", id);
        return egitimRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Egitim : {}", id);
        egitimRepository.deleteById(id);
    }
}
