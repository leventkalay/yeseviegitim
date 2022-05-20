package com.yesevi.egitim.repository;

import com.yesevi.egitim.domain.Egitmen;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Egitmen entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EgitmenRepository extends JpaRepository<Egitmen, Long> {}
