package com.gestionformation.repository;

import com.gestionformation.domain.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedBackRepository extends JpaRepository<Feedback, Long> {
}
