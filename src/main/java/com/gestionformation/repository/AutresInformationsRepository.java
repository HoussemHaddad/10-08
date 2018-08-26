package com.gestionformation.repository;

import com.gestionformation.domain.AutresInformations;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the AutresInformations entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AutresInformationsRepository extends JpaRepository<AutresInformations, Long> {

    @Query("select a from AutresInformations a where a.reservation.id=:x and a.nomInfo like 'DateFormation'")
    public AutresInformations chercherAutreInfo(@Param("x")Long mc);

}
