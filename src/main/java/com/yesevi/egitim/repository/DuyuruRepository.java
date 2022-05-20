package com.yesevi.egitim.repository;

import com.yesevi.egitim.domain.Duyuru;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Duyuru entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DuyuruRepository extends JpaRepository<Duyuru, Long> {}
