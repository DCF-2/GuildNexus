package com.ifpe.edu.br.guildnexus.dtos;

public record CreateCharacterDTO(String name, Integer level, String characterClass, Long gameId) {
}