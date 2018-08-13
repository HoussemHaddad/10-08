package com.gestionformation.repository;

import com.gestionformation.domain.CentreDeFormation;
import com.gestionformation.domain.Commentaire;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Commentaire entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CommentaireRepository extends JpaRepository<Commentaire, Long> {

    @Query("select c from Commentaire c  where  c.commentaire.id=:y ")
    //@Query("select r.role from Role r where r.secretaire.login like :x ")
    public Page<Commentaire> chercher( @Param("y")Long mc1,Pageable pageable);

}
