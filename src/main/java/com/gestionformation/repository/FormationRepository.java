package com.gestionformation.repository;


import com.gestionformation.domain.Formation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Formation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FormationRepository extends JpaRepository<Formation, Long> {
    @Query("select f from Formation f  where  f.nomFormation like :x ")
    //@Query("select r.role from Role r where r.secretaire.login like :x ")
    public Page<Formation> chercher(@Param("x")String mc, Pageable pageable);

}
