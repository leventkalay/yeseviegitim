package com.yesevi.egitim.repository;

import com.yesevi.egitim.domain.Kurum;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Kurum entity.
 */
@SuppressWarnings("unused")
@Repository
public interface KurumRepository extends JpaRepository<Kurum, Long> {}
