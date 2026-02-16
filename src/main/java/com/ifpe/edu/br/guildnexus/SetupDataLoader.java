package com.ifpe.edu.br.guildnexus;

import com.ifpe.edu.br.guildnexus.entities.Game;
import com.ifpe.edu.br.guildnexus.entities.Gamer;
import com.ifpe.edu.br.guildnexus.repositories.GameRepository;
import com.ifpe.edu.br.guildnexus.repositories.GamerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;

@Configuration
public class SetupDataLoader implements CommandLineRunner {

    @Autowired
    GameRepository gameRepository;

    @Autowired
    GamerRepository gamerRepository;

    @Override
    public void run(String... args) throws Exception {
        // 1. Cria os Jogos se não existirem
        if (gameRepository.count() == 0) {
            gameRepository.saveAll(Arrays.asList(
                    new Game(null, "World of Warcraft", "MMORPG"),
                    new Game(null, "League of Legends", "MOBA"),
                    new Game(null, "CS:GO", "FPS"),
                    new Game(null, "Tibia", "MMORPG")
            ));
        }

        // 2. Cria um Gamer de Teste se não existir
        if (gamerRepository.findByEmail("teste@gmail.com").isEmpty()) {
            Gamer gamer = new Gamer();
            gamer.setName("Gamer de Teste");
            gamer.setEmail("teste@gmail.com");
            // Senha: "123" (criptografada)
            gamer.setPassword(new BCryptPasswordEncoder().encode("123"));
            
            gamerRepository.save(gamer);
            System.out.println(">>> Usuário de Teste criado: teste@gmail.com / 123");
        }
    }
}