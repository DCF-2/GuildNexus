package com.ifpe.edu.br.guildnexus.entities;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_characters")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Character {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private Integer level;
    
    private String characterClass; // Ex: Mago, Guerreiro

    // Relacionamento: Vários personagens pertencem a UM Gamer
    @ManyToOne
    @JoinColumn(name = "gamer_id")
    private Gamer gamer;

    // Relacionamento: Vários personagens jogam UM Jogo
    @ManyToOne
    @JoinColumn(name = "game_id")
    private Game game;

    // Quem EU sigo
    @JsonIgnore // Evita que ao carregar o personagem, carregue a lista infinita de seguidores
    @ManyToMany
    @JoinTable(
            name = "tb_followers",
            joinColumns = @JoinColumn(name = "follower_id"), // Eu (Seguidor)
            inverseJoinColumns = @JoinColumn(name = "followed_id") // Quem eu sigo
    )
    private List<Character> following;

    // Quem ME segue
    @JsonIgnore
    @ManyToMany(mappedBy = "following")
    private List<Character> followers;
}