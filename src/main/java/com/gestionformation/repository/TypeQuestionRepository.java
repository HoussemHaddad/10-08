package com.gestionformation.repository;

import com.gestionformation.domain.TypeQuestion;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the TypeQuestion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TypeQuestionRepository extends JpaRepository<TypeQuestion, Long> {

}
