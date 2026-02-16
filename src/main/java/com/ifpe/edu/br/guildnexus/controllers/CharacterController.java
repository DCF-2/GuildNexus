package com.ifpe.edu.br.guildnexus.controllers;

import com.ifpe.edu.br.guildnexus.dtos.CreateCharacterDTO;
import com.ifpe.edu.br.guildnexus.entities.Character;
import com.ifpe.edu.br.guildnexus.entities.Game;
import com.ifpe.edu.br.guildnexus.entities.Gamer;
import com.ifpe.edu.br.guildnexus.repositories.CharacterRepository;
import com.ifpe.edu.br.guildnexus.repositories.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/characters")
public class CharacterController {

    @Autowired
    private CharacterRepository characterRepository;

    @Autowired
    private GameRepository gameRepository;

    @PostMapping
    public ResponseEntity create(@RequestBody @jakarta.validation.Valid CreateCharacterDTO dto) {
        // 1. Pega o Gamer autenticado (extraído do Token JWT)
        Gamer gamer = (Gamer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // 2. Busca o Jogo pelo ID
        Game game = gameRepository.findById(dto.gameId())
                .orElseThrow(() -> new RuntimeException("Jogo não encontrado"));

        // 3. Monta o Personagem
        Character newChar = new Character();
        newChar.setName(dto.name());
        newChar.setLevel(dto.level());
        newChar.setCharacterClass(dto.characterClass());
        newChar.setGame(game);
        newChar.setGamer(gamer); // Associa ao dono do token

        characterRepository.save(newChar);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/my")
    public ResponseEntity<List<Character>> listMyCharacters() {
        Gamer gamer = (Gamer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var list = characterRepository.findByGamerId(gamer.getId());
        return ResponseEntity.ok(list);
    }

    @PostMapping("/follow")
    @org.springframework.transaction.annotation.Transactional
    public ResponseEntity followToggle(@RequestBody com.ifpe.edu.br.guildnexus.dtos.FollowDTO dto) {
        // 1. Segurança básica (Gamer logado é dono do 'myCharacterId'?)
        Gamer loggedGamer = (Gamer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Character me = characterRepository.findById(dto.myCharacterId())
                .orElseThrow(() -> new RuntimeException("Seu personagem não encontrado"));

        if (!me.getGamer().getId().equals(loggedGamer.getId())) {
            return ResponseEntity.status(403).body("Esse personagem não é seu!");
        }

        // 2. Carrega o alvo
        Character target = characterRepository.findById(dto.targetCharacterId())
                .orElseThrow(() -> new RuntimeException("Alvo não encontrado"));

        // 3. Lógica de Toggle (Seguir / Deixar de seguir)
        if (me.getFollowing().contains(target)) {
            me.getFollowing().remove(target);
            characterRepository.save(me);
            return ResponseEntity.ok("Deixou de seguir " + target.getName());
        } else {
            me.getFollowing().add(target);
            characterRepository.save(me);
            return ResponseEntity.ok("Agora seguindo " + target.getName());
        }
    }
}