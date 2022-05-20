package com.yesevi.egitim.service.impl;

import com.yesevi.egitim.domain.Ders;
import com.yesevi.egitim.repository.DersRepository;
import com.yesevi.egitim.service.DersService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Ders}.
 */
@Service
@Transactional
public class DersServiceImpl implements DersService {

    private final Logger log = LoggerFactory.getLogger(DersServiceImpl.class);

    private final DersRepository dersRepository;

    public DersServiceImpl(DersRepository dersRepository) {
        this.dersRepository = dersRepository;
    }

    @Override
    public Ders save(Ders ders) {
        log.debug("Request to save Ders : {}", ders);
        return dersRepository.save(ders);
    }

    @Override
    public Optional<Ders> partialUpdate(Ders ders) {
        log.debug("Request to partially update Ders : {}", ders);

        return dersRepository
            .findById(ders.getId())
            .map(existingDers -> {
                if (ders.getAdi() != null) {
                    existingDers.setAdi(ders.getAdi());
                }
                if (ders.getAciklama() != null) {
                    existingDers.setAciklama(ders.getAciklama());
                }
                if (ders.getDersLinki() != null) {
                    existingDers.setDersLinki(ders.getDersLinki());
                }
                if (ders.getDersVideosu() != null) {
                    existingDers.setDersVideosu(ders.getDersVideosu());
                }
                if (ders.getDersVideosuContentType() != null) {
                    existingDers.setDersVideosuContentType(ders.getDersVideosuContentType());
                }

                return existingDers;
            })
            .map(dersRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Ders> findAll(Pageable pageable) {
        log.debug("Request to get all Ders");
        return dersRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Ders> findOne(Long id) {
        log.debug("Request to get Ders : {}", id);
        return dersRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Ders : {}", id);
        dersRepository.deleteById(id);
    }
}
