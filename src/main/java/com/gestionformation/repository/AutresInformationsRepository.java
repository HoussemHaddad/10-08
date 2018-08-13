package com.gestionformation.repository;

import com.gestionformation.domain.AutresInformations;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the AutresInformations entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AutresInformationsRepository extends JpaRepository<AutresInformations, Long> {

}
