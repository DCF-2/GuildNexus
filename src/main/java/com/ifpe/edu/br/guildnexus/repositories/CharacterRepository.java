package com.ifpe.edu.br.guildnexus.repositories;

import com.ifpe.edu.br.guildnexus.entities.Character;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CharacterRepository extends JpaRepository<Character, Long> {
    // Buscar todos os personagens de um Gamer específico
    List<Character> findByGamerId(Long gamerId);
}