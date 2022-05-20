package com.yesevi.egitim.service.impl;

import com.yesevi.egitim.domain.Kurum;
import com.yesevi.egitim.repository.KurumRepository;
import com.yesevi.egitim.service.KurumService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Kurum}.
 */
@Service
@Transactional
public class KurumServiceImpl implements KurumService {

    private final Logger log = LoggerFactory.getLogger(KurumServiceImpl.class);

    private final KurumRepository kurumRepository;

    public KurumServiceImpl(KurumRepository kurumRepository) {
        this.kurumRepository = kurumRepository;
    }

    @Override
    public Kurum save(Kurum kurum) {
        log.debug("Request to save Kurum : {}", kurum);
        return kurumRepository.save(kurum);
    }

    @Override
    public Optional<Kurum> partialUpdate(Kurum kurum) {
        log.debug("Request to partially update Kurum : {}", kurum);

        return kurumRepository
            .findById(kurum.getId())
            .map(existingKurum -> {
                if (kurum.getAdi() != null) {
                    existingKurum.setAdi(kurum.getAdi());
                }
                if (kurum.getAciklama() != null) {
                    existingKurum.setAciklama(kurum.getAciklama());
                }
                if (kurum.getAktif() != null) {
                    existingKurum.setAktif(kurum.getAktif());
                }

                return existingKurum;
            })
            .map(kurumRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Kurum> findAll(Pageable pageable) {
        log.debug("Request to get all Kurums");
        return kurumRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Kurum> findOne(Long id) {
        log.debug("Request to get Kurum : {}", id);
        return kurumRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Kurum : {}", id);
        kurumRepository.deleteById(id);
    }
}
