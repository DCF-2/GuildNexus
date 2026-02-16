package com.ifpe.edu.br.guildnexus.controllers;

import com.ifpe.edu.br.guildnexus.dtos.ChatMessageDTO;
import com.ifpe.edu.br.guildnexus.dtos.SignalMessageDTO;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    // --- CHAT ---
    // Cliente envia para: /app/chat/{liveId}
    // Servidor repassa para: /topic/chat/{liveId}
    @MessageMapping("/chat/{liveId}")
    @SendTo("/topic/chat/{liveId}")
    public ChatMessageDTO handleChatMessage(@DestinationVariable Long liveId, @Payload ChatMessageDTO message) {
        // Aqui você poderia salvar o histórico no banco se quisesse
        System.out.println("Chat na Live " + liveId + ": " + message.content());
        return message; // Apenas repassa para todos na sala
    }

    // --- VÍDEO (SINALIZAÇÃO WEBRTC) ---
    // Funciona como um "telefone": Um peer manda os dados de conexão, o servidor repassa pro outro.
    
    // Streamer envia dados da oferta (Offer)
    @MessageMapping("/video/signal/{liveId}")
    @SendTo("/topic/video/{liveId}")
    public SignalMessageDTO handleVideoSignal(@DestinationVariable Long liveId, @Payload SignalMessageDTO signal) {
        // Log simples para debug
        System.out.println("Sinal WebRTC recebido (" + signal.type() + ") de: " + signal.senderId());
        return signal;
    }
}