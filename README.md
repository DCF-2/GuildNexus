# 🛡️ Guild Nexus

Um sistema de gerenciamento de personagens e rede social focado em jogadores de MMO. Desenvolvido como projeto para a disciplina de Web 3 do IFPE.

O **Guild Nexus** permite que gamers se cadastrem, criem seus personagens (com upload de avatares) e interajam em um feed global. Além disso, a plataforma conta com um sistema de Live Streaming integrado para compartilhamento de tela e chat em tempo real.

---

## 🎥 Demonstração do Sistema

Confira abaixo o vídeo demonstrando o fluxo completo (Cadastro, Login, Busca, Criação e Live):

[![Assista ao Vídeo](https://i.ytimg.com)]()

*Sistema do Guild Nexus*

---

## 🚀 Funcionalidades

- **Autenticação e Segurança:** Login e Cadastro com criptografia de senhas e autenticação via JWT (JSON Web Token).
- **Gerenciamento de Personagens:** Criação de múltiplos personagens por conta, vinculação a jogos dinâmicos e upload de imagens (Base64).
- **Rede Social (Feed):** Sistema de postagens, comentários e curtidas.
- **Conexões:** Sistema de seguidores (Followers/Following) com feed personalizado.
- **Live Stream e Chat:** Transmissão de gameplay (partilha de tela e câmera via WebRTC) e chat global em tempo real (via WebSockets/STOMP).

---

## 🛠️ Tecnologias Utilizadas

**Backend:**
- Java 21
- Spring Boot 3
- Spring Security (JWT)
- Spring Data JPA
- Banco de Dados H2 (Memória)
- WebSockets (STOMP)

**Frontend:**
- HTML5, CSS3, JavaScript (Vanilla)
- Bootstrap 5
- SockJS & StompJS
- WebRTC (Nativo do Browser)

---

## ⚙️ Como Executar o Projeto

1. Clone este repositório:
   ```bash
   git clone [https://github.com/SEU-USUARIO/guildnexus.git](https://github.com/SEU-USUARIO/guildnexus.git)
   ```
2. Entre na pasta do projeto:
   ```bash
   cd guildnexus
   ```
3. Execute a aplicação usando o Maven Wrapper:
   ```bash
   ./mvnw spring-boot:run
   ```
4. Acesse no seu navegador:
   ```text
   http://localhost:8080
   ```

   ---

## 👨‍💻 Autor

Desenvolvido por **Davi Freitas** && **Marcos André**.  
_Curso de Análise e Desenvolvimento de Sistemas – IFPE_
