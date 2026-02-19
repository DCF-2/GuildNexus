package com.ifpe.edu.br.guildnexus.repositories;

import com.ifpe.edu.br.guildnexus.entities.Game;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {
    // Busca o jogo ignorando letras maiúsculas/minúsculas
    Optional<Game> findByNameIgnoreCase(String name);
}