package com.ifpe.edu.br.guildnexus.controllers;

import com.ifpe.edu.br.guildnexus.dtos.CreatePostDTO;
import com.ifpe.edu.br.guildnexus.entities.Character;
import com.ifpe.edu.br.guildnexus.entities.Gamer;
import com.ifpe.edu.br.guildnexus.entities.Post;
import com.ifpe.edu.br.guildnexus.repositories.CharacterRepository;
import com.ifpe.edu.br.guildnexus.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CharacterRepository characterRepository;

    @PostMapping
    public ResponseEntity create(@RequestBody CreatePostDTO dto) {
        // 1. Quem é o usuário logado?
        Gamer loggedGamer = (Gamer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // 2. Busca o personagem que "quer" postar
        Character character = characterRepository.findById(dto.characterId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Personagem não encontrado"));

        // 3. SEGURANÇA: O personagem pertence ao usuário logado?
        if (!character.getGamer().getId().equals(loggedGamer.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Você não é dono deste personagem!");
        }

        // 4. Salva o post
        Post newPost = new Post();
        newPost.setContent(dto.content());
        newPost.setAuthor(character);
        
        postRepository.save(newPost);

        return ResponseEntity.ok().build();
    }

    // Listar todos os posts (Feed Global simples por enquanto)
    @GetMapping
    public ResponseEntity<List<Post>> listAll() {
        return ResponseEntity.ok(postRepository.findAll());
    }
}