package com.ifpe.edu.br.guildnexus.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreatePostDTO(
    @NotBlank(message = "O post não pode ser vazio")
    @Size(max = 500, message = "O post não pode ter mais de 500 caracteres")
    String content,

    @NotNull(message = "O ID do personagem é obrigatório")
    Long characterId
) {}