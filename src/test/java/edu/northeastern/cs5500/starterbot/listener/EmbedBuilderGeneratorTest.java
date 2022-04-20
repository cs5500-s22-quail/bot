package edu.northeastern.cs5500.starterbot.listener;

import static org.junit.jupiter.api.Assertions.*;

import edu.northeastern.cs5500.starterbot.controller.UserPokemonController;
import edu.northeastern.cs5500.starterbot.repository.InMemoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EmbedBuilderGeneratorTest {

    EmbedBuilderGenerator embedBuilderGenerator;

    @BeforeEach
    void setUp() {
        this.embedBuilderGenerator = new EmbedBuilderGenerator();
        this.embedBuilderGenerator.userPokemonController =
                new UserPokemonController(new InMemoryRepository<>());
    }

    @Test
    void getFightPokemonEmbeds() {}

    @Test
    void getAllPokemonEmbeds() {
        assertNotNull(this.embedBuilderGenerator.getAllPokemonEmbeds("7777"));
    }
}
