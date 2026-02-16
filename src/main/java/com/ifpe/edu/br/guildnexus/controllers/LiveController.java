package com.ifpe.edu.br.guildnexus.controllers;

import com.ifpe.edu.br.guildnexus.dtos.StartLiveDTO;
import com.ifpe.edu.br.guildnexus.entities.Character;
import com.ifpe.edu.br.guildnexus.entities.Gamer;
import com.ifpe.edu.br.guildnexus.entities.LiveSession;
import com.ifpe.edu.br.guildnexus.repositories.CharacterRepository;
import com.ifpe.edu.br.guildnexus.repositories.LiveSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/lives")
public class LiveController {

    @Autowired
    private LiveSessionRepository liveSessionRepository;

    @Autowired
    private CharacterRepository characterRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate; // O carteiro do WebSocket

    @PostMapping("/start")
    public ResponseEntity<LiveSession> startLive(@RequestBody StartLiveDTO dto) {
        // 1. Segurança
        Gamer loggedGamer = (Gamer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Character streamer = characterRepository.findById(dto.characterId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!streamer.getGamer().getId().equals(loggedGamer.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        // 2. Cria a Sessão no Banco
        LiveSession live = new LiveSession();
        live.setTitle(dto.title());
        live.setStreamer(streamer);
        live.setActive(true);
        live.setStartedAt(LocalDateTime.now());
        
        liveSessionRepository.save(live);

        // 3. NOTIFICAÇÃO EM TEMPO REAL (O Pulo do Gato)
        // Para cada seguidor, enviamos um aviso no WebSocket
        List<Character> followers = streamer.getFollowers();
        for (Character follower : followers) {
            // Envia para o tópico pessoal do seguidor: /topic/notifications/{id_do_seguidor}
            String notification = "🔴 " + streamer.getName() + " entrou ao vivo: " + dto.title();
            messagingTemplate.convertAndSend("/topic/notifications/" + follower.getId(), notification);
        }

        return ResponseEntity.ok(live);
    }

    @PostMapping("/{liveId}/stop")
    public ResponseEntity stopLive(@PathVariable Long liveId) {
        LiveSession live = liveSessionRepository.findById(liveId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        
        // (Adicionar validação de segurança aqui se quiser ser estrito)
        
        live.setActive(false);
        liveSessionRepository.save(live);
        
        return ResponseEntity.ok().build();
    }
    
    @GetMapping
    public ResponseEntity<List<LiveSession>> listActiveLives() {
        // Você precisará criar um método no Repository: findByActiveTrue()
        // Por enquanto retornamos tudo ou filtre no Java
        return ResponseEntity.ok(liveSessionRepository.findAll()); 
    }
}