package com.gestionformation.repository;

import com.gestionformation.domain.TypeDeNotification;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the TypeDeNotification entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TypeDeNotificationRepository extends JpaRepository<TypeDeNotification, Long> {

    @Query("select n from TypeDeNotification n where n.Type like 'ReservationRF'")
    public TypeDeNotification chercherReservationRF();

}
