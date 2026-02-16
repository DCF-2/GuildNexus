package com.ifpe.edu.br.guildnexus.repositories;

import com.ifpe.edu.br.guildnexus.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    // Buscar posts de um personagem (perfil)
    List<Post> findByAuthorIdOrderByCreatedAtDesc(Long characterId);

    // Tradução: Selecione Posts onde o Autor ESTEJA NA lista de "seguidos" do meu personagem
    @Query("SELECT p FROM Post p WHERE p.author IN (SELECT f FROM Character c JOIN c.following f WHERE c.id = :myCharacterId) ORDER BY p.createdAt DESC")
    List<Post> findFeedByCharacterId(Long myCharacterId);
}