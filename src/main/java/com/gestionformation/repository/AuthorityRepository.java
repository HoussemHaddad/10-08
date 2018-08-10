package com.gestionformation.repository;

import com.gestionformation.domain.Authority;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Authority entity.
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {

    @Query("select r from Authority r where r.name like :x")
    public Page<Authority> chercher(@Param("x") String mc, Pageable pageable);

    @Query("select name from Authority r")
    public List<String> chercherRole();
}
