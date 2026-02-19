package com.ifpe.edu.br.guildnexus.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_posts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT") // Permite textos longos
    private String content;

    @CreationTimestamp // Pega a hora do servidor automaticamente
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "character_id")
    private Character author; // Quem escreveu o post
}