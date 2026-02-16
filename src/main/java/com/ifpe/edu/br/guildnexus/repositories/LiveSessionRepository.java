package com.ifpe.edu.br.guildnexus.repositories;
import com.ifpe.edu.br.guildnexus.entities.LiveSession;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface LiveSessionRepository extends JpaRepository<LiveSession, Long> {
    Optional<LiveSession> findByStreamerIdAndActiveTrue(Long streamerId);
}