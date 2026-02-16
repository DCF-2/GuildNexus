package com.ifpe.edu.br.guildnexus.repositories;

import com.ifpe.edu.br.guildnexus.entities.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {
}