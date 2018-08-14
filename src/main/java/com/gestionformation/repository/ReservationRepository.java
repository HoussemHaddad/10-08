package com.gestionformation.repository;

import com.gestionformation.domain.Reservation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Reservation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
//    @Query("select r from Reservation r  ")
    @Query("SELECT r from Reservation r JOIN r.autresInformations a WHERE (r.id = a.reservation.id) and r.utilisateur.id=:x")
    //@Query("select r.role from Role r where r.secretaire.login like :x ")
    public Page<Reservation> chercherReservation(@Param("x")Long mc, Pageable pageable);

//    @Query(value = "select distinct reservation from Reservation reservation left join fetch reservation.questions",
//        countQuery = "select count(distinct reservation) from Reservation reservation")
//    Page<Reservation> findAllWithEagerRelationships(Pageable pageable);
//
//    @Query(value = "select distinct reservation from Reservation reservation left join fetch reservation.questions")
//    List<Reservation> findAllWithEagerRelationships();
//
//    @Query("select reservation from Reservation reservation left join fetch reservation.questions where reservation.id =:id")
//    Optional<Reservation> findOneWithEagerRelationships(@Param("id") Long id);

}
