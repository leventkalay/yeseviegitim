package com.yesevi.egitim.repository;

import com.yesevi.egitim.domain.OgrenciEgitimler;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the OgrenciEgitimler entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OgrenciEgitimlerRepository extends JpaRepository<OgrenciEgitimler, Long> {}
