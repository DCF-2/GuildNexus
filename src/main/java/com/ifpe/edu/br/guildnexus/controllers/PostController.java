package com.ifpe.edu.br.guildnexus.controllers;

import com.ifpe.edu.br.guildnexus.dtos.AddCommentDTO; // Importante
import com.ifpe.edu.br.guildnexus.dtos.AddLikeDTO;    // Importante
import com.ifpe.edu.br.guildnexus.dtos.CreatePostDTO;
import com.ifpe.edu.br.guildnexus.entities.*;         // Importa Post, Comment, PostLike, etc.
import com.ifpe.edu.br.guildnexus.entities.Character;
import com.ifpe.edu.br.guildnexus.repositories.CharacterRepository;
import com.ifpe.edu.br.guildnexus.repositories.CommentRepository; // Importante
import com.ifpe.edu.br.guildnexus.repositories.PostLikeRepository; // Importante
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

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostLikeRepository postLikeRepository;


    // --- POSTAGENS ---

    @PostMapping
    public ResponseEntity create(@RequestBody @jakarta.validation.Valid CreatePostDTO dto) {
        Gamer loggedGamer = (Gamer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Character character = characterRepository.findById(dto.characterId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Personagem não encontrado"));

        if (!character.getGamer().getId().equals(loggedGamer.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Você não é dono deste personagem!");
        }

        Post newPost = new Post();
        newPost.setContent(dto.content());
        newPost.setAuthor(character);
        
        postRepository.save(newPost);

        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<Post>> listAll() {
        return ResponseEntity.ok(postRepository.findAll());
    }


    // --- COMENTÁRIOS ---

    @PostMapping("/{postId}/comments")
    public ResponseEntity addComment(@PathVariable Long postId, @RequestBody AddCommentDTO dto) {
        // 1. Validação de Segurança
        Gamer loggedGamer = (Gamer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Character author = characterRepository.findById(dto.characterId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Personagem não encontrado"));

        if (!author.getGamer().getId().equals(loggedGamer.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Personagem não pertence a você");
        }

        // 2. Achar o post
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post não encontrado"));

        // 3. Salvar
        Comment comment = new Comment();
        comment.setContent(dto.content());
        comment.setPost(post);
        comment.setAuthor(author);
        commentRepository.save(comment);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{postId}/comments")
    public ResponseEntity<List<Comment>> listComments(@PathVariable Long postId) {
        return ResponseEntity.ok(commentRepository.findByPostId(postId));
    }


    // --- LIKES ---

    @PostMapping("/{postId}/like")
    @org.springframework.transaction.annotation.Transactional
    public ResponseEntity toggleLike(@PathVariable Long postId, @RequestBody AddLikeDTO dto) {
        // 1. Segurança
        Gamer loggedGamer = (Gamer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Character author = characterRepository.findById(dto.characterId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!author.getGamer().getId().equals(loggedGamer.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        // 2. Lógica de Toggle
        if (postLikeRepository.existsByPostIdAndAuthorId(postId, dto.characterId())) {
            postLikeRepository.deleteByPostIdAndAuthorId(postId, dto.characterId());
            return ResponseEntity.ok("Like removido");
        } else {
            Post post = postRepository.findById(postId).orElseThrow();
            PostLike like = new PostLike();
            like.setPost(post);
            like.setAuthor(author);
            postLikeRepository.save(like);
            return ResponseEntity.ok("Like adicionado");
        }
    }

    // Listar Feed Personalizado (Posts de quem eu sigo)
    @GetMapping("/feed/{myCharacterId}")
    public ResponseEntity listFeed(@PathVariable Long myCharacterId) {
        // Segurança: Só posso ver o feed SE o personagem for meu
        Gamer loggedGamer = (Gamer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Character me = characterRepository.findById(myCharacterId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!me.getGamer().getId().equals(loggedGamer.getId())) {
             return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        List<Post> feed = postRepository.findFeedByCharacterId(myCharacterId);
        return ResponseEntity.ok(feed);
    }

    @GetMapping("/character/{characterId}")
    public ResponseEntity<List<Post>> listByCharacter(@PathVariable Long characterId) {
        return ResponseEntity.ok(postRepository.findByAuthorIdOrderByCreatedAtDesc(characterId));
    }
}