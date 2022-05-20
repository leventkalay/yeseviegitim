package com.yesevi.egitim.repository;

import com.yesevi.egitim.domain.EgitimDersler;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the EgitimDersler entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EgitimDerslerRepository extends JpaRepository<EgitimDersler, Long> {}
