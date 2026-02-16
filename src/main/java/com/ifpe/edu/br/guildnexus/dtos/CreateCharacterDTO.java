package com.ifpe.edu.br.guildnexus.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateCharacterDTO(
    @NotBlank(message = "O nome do personagem não pode ser vazio")
    String name,
    
    @NotNull(message = "O nível é obrigatório")
    Integer level,
    
    @NotBlank(message = "A classe é obrigatória")
    String characterClass,
    
    @NotNull(message = "O ID do jogo é obrigatório")
    Long gameId
) {}