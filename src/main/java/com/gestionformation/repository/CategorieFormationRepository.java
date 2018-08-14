package com.gestionformation.repository;

import com.gestionformation.domain.CategorieFormation;

import com.gestionformation.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data  repository for the CategorieFormation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CategorieFormationRepository extends JpaRepository<CategorieFormation, Long> {

    @Query("select c from CategorieFormation c  where  c.nomCategorie like :x ")
    //@Query("select r.role from Role r where r.secretaire.login like :x ")
    public Page<CategorieFormation> chercher(@Param("x")String mc, Pageable pageable);

    @Query("select c from CategorieFormation c ")
    public List<CategorieFormation> chercherCategorieF();
}


