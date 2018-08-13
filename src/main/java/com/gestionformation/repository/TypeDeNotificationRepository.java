package com.gestionformation.repository;

import com.gestionformation.domain.TypeDeNotification;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the TypeDeNotification entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TypeDeNotificationRepository extends JpaRepository<TypeDeNotification, Long> {

}
