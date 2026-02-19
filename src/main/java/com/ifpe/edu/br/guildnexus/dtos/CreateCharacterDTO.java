package com.ifpe.edu.br.guildnexus.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateCharacterDTO(
    @NotBlank(message = "O nome não pode ser vazio") String name,
    @NotNull(message = "O nível é obrigatório") Integer level,
    @NotBlank(message = "A classe é obrigatória") String characterClass,
    @NotBlank(message = "O nome do jogo é obrigatório") String gameName, // Mudou aqui
    String photoUrl
) {}