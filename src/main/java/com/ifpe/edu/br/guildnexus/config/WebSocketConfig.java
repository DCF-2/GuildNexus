package com.ifpe.edu.br.guildnexus.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Habilita um broker simples em memória para enviar mensagens de volta para o cliente
        // Prefixo para mensagens que vão DO SERVIDOR -> CLIENTE
        config.enableSimpleBroker("/topic");
        
        // Prefixo para mensagens que vão DO CLIENTE -> SERVIDOR
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Define o endpoint onde o Front vai se conectar (o "handshake")
        // Ex: var socket = new SockJS('/ws');
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*") // Libera CORS para o Front (React/Angular/HTML)
                .withSockJS(); // Habilita SockJS para navegadores que não suportam WS puro
    }
    
    public void addViewControllers(ViewControllerRegistry registry) {
        // Diz: Quando acessar "http://localhost:8080/", encaminhe internamente para "/index.html"
        registry.addViewController("/").setViewName("forward:/index.html");
    }
}