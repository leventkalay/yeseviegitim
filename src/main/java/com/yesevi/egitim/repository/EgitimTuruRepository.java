package com.yesevi.egitim.repository;

import com.yesevi.egitim.domain.EgitimTuru;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the EgitimTuru entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EgitimTuruRepository extends JpaRepository<EgitimTuru, Long> {}
