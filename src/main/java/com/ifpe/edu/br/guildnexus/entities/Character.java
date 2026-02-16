package com.ifpe.edu.br.guildnexus.entities;

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
}