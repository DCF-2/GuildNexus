package com.ifpe.edu.br.guildnexus.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_lives")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LiveSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    
    private boolean active;

    private LocalDateTime startedAt;

    @OneToOne
    @JoinColumn(name = "character_id")
    private Character streamer; // Quem está transmitindo
}