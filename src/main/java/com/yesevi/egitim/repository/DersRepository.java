package com.yesevi.egitim.repository;

import com.yesevi.egitim.domain.Ders;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Ders entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DersRepository extends JpaRepository<Ders, Long> {}
