package com.ifpe.edu.br.guildnexus;

import com.ifpe.edu.br.guildnexus.entities.Game;
import com.ifpe.edu.br.guildnexus.repositories.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class SetupDataLoader implements CommandLineRunner {

    @Autowired
    GameRepository gameRepository;

    @Override
    public void run(String... args) throws Exception {
        if (gameRepository.count() == 0) {
            gameRepository.saveAll(Arrays.asList(
                    new Game(null, "World of Warcraft", "MMORPG"),
                    new Game(null, "League of Legends", "MOBA"),
                    new Game(null, "CS:GO", "FPS"),
                    new Game(null, "Tibia", "MMORPG")
            ));
        }
    }
}