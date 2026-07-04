package com.management.finito.feedback;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface FeedbackJPARepository extends JpaRepository<Feedback, UUID> {
    List<Feedback> findTop200ByOrderByQuandoDesc();
}
