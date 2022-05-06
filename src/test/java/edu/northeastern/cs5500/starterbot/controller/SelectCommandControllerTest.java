package edu.northeastern.cs5500.starterbot.controller;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import edu.northeastern.cs5500.starterbot.repository.InMemoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SelectCommandControllerTest {

    UserPokemonController userPokemonController;
    SelectCommandController selectCommandController;

    @BeforeEach
    void setUp() {
        userPokemonController = new UserPokemonController(new InMemoryRepository<>());
        selectCommandController = new SelectCommandController(userPokemonController);
    }

    @Test
    void getSelectEmbeds() {
        assertThat(userPokemonController.getUserPokemonForMemberId("7777")).isNotNull();

        assertThat(selectCommandController.getSelectEmbeds("7777", "0")).isNotNull();
        assertThat(selectCommandController.getSelectEmbeds("7777", "1")).isNotNull();
        assertThat(selectCommandController.getSelectEmbeds("7777", "-1")).isNotNull();
        assertThat(selectCommandController.getSelectEmbeds("7777", "not int")).isNotNull();
    }
}
