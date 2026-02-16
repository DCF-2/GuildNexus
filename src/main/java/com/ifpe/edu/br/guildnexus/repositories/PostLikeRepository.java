package com.ifpe.edu.br.guildnexus.repositories;

import com.ifpe.edu.br.guildnexus.entities.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    // Verifica se esse personagem JÁ curtiu esse post
    boolean existsByPostIdAndAuthorId(Long postId, Long authorId);
    
    // Para descurtir (remover o like)
    void deleteByPostIdAndAuthorId(Long postId, Long authorId);
}