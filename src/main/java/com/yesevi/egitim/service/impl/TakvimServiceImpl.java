package com.yesevi.egitim.service.impl;

import com.yesevi.egitim.domain.Takvim;
import com.yesevi.egitim.repository.TakvimRepository;
import com.yesevi.egitim.service.TakvimService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Takvim}.
 */
@Service
@Transactional
public class TakvimServiceImpl implements TakvimService {

    private final Logger log = LoggerFactory.getLogger(TakvimServiceImpl.class);

    private final TakvimRepository takvimRepository;

    public TakvimServiceImpl(TakvimRepository takvimRepository) {
        this.takvimRepository = takvimRepository;
    }

    @Override
    public Takvim save(Takvim takvim) {
        log.debug("Request to save Takvim : {}", takvim);
        return takvimRepository.save(takvim);
    }

    @Override
    public Optional<Takvim> partialUpdate(Takvim takvim) {
        log.debug("Request to partially update Takvim : {}", takvim);

        return takvimRepository
            .findById(takvim.getId())
            .map(existingTakvim -> {
                if (takvim.getAdi() != null) {
                    existingTakvim.setAdi(takvim.getAdi());
                }

                return existingTakvim;
            })
            .map(takvimRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Takvim> findAll(Pageable pageable) {
        log.debug("Request to get all Takvims");
        return takvimRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Takvim> findOne(Long id) {
        log.debug("Request to get Takvim : {}", id);
        return takvimRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Takvim : {}", id);
        takvimRepository.deleteById(id);
    }
}
