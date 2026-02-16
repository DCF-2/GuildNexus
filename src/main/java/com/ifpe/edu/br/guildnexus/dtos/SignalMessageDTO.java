package com.ifpe.edu.br.guildnexus.dtos;

// O 'type' pode ser "offer", "answer" ou "candidate"
// O 'data' é o payload técnico do WebRTC (SDP ou ICE Candidate)
public record SignalMessageDTO(String type, String data, String senderId) {
}