package com.ifpe.edu.br.guildnexus.repositories;

import com.ifpe.edu.br.guildnexus.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    // Buscar posts de um personagem específico (para ver o perfil dele)
    List<Post> findByAuthorIdOrderByCreatedAtDesc(Long characterId);
}