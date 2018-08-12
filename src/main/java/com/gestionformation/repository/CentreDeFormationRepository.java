package com.gestionformation.repository;


import com.gestionformation.domain.CategorieFormation;
import com.gestionformation.domain.CentreDeFormation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data  repository for the CentreDeFormation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CentreDeFormationRepository extends JpaRepository<CentreDeFormation, Long> {

    @Query("select c from CentreDeFormation c  where  c.nomCentre like :x ")
    //@Query("select r.role from Role r where r.secretaire.login like :x ")
    public Page<CentreDeFormation> chercher(@Param("x")String mc, Pageable pageable);

    @Query("select c from CentreDeFormation c")
    public List<CentreDeFormation> chercherCentre();

}
