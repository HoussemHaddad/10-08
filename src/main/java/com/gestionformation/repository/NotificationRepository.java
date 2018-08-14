package com.gestionformation.repository;

import com.gestionformation.domain.CategorieFormation;
import com.gestionformation.domain.Notification;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Notification entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Query("select n from Notification n  where n.utilisateur.id=:x AND n.etatDeVue is false")
    //@Query("select r.role from Role r where r.secretaire.login like :x ")
    public Page<Notification> chercher(@Param("x")Long mc, Pageable pageable);

}
