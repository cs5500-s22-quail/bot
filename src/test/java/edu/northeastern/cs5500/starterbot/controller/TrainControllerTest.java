package edu.northeastern.cs5500.starterbot.controller;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import edu.northeastern.cs5500.starterbot.model.PokemonInfo;
import edu.northeastern.cs5500.starterbot.model.UserPokemon;
import edu.northeastern.cs5500.starterbot.model.WildPokemon;
import edu.northeastern.cs5500.starterbot.repository.InMemoryRepository;
import edu.northeastern.cs5500.starterbot.service.PokemonService;
import net.dv8tion.jda.api.EmbedBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TrainControllerTest {

    UserPokemonController userPokemonController;
    TrainController trainController;
    WildPokemon wildPokemon1;
    PokemonInfo wildPokemonInfo1;
    WildPokemon wildPokemon2;

    @BeforeEach
    void setUp() {
        userPokemonController = new UserPokemonController(new InMemoryRepository<>());
        trainController = new TrainController(userPokemonController);
        PokemonService pokemonService = new PokemonService();
        PokemonGenerator pokemonGenerator = new PokemonGenerator(pokemonService);
        wildPokemon1 = pokemonGenerator.getWildPokemon();
        wildPokemon2 = pokemonGenerator.getWildPokemon();
        wildPokemonInfo1 = wildPokemon1.getPokemonInfo();
    }

    @Test
    void getFightPokemonEmbeds() {
        assertThat(trainController.getFightPokemonEmbeds(wildPokemon1)).isNotNull();
    }

    @Test
    void getFightResultEmbeds() {
        EmbedBuilder eb = trainController.getFightPokemonEmbeds(wildPokemon1);
        PokemonInfo wildPokemonInfo2 = wildPokemon2.getPokemonInfo();
        UserPokemon userPokemon = userPokemonController.getUserPokemonForMemberId("7777");
        userPokemon.setCarriedPokemon(wildPokemonInfo2);
        assertThat(trainController.getFightResultEmbeds(eb, wildPokemonInfo1, userPokemon))
                .isNotNull();
    }

    @Test
    void getLevelUpEmbeds() {
        assertThat(trainController.getLevelUpEmbeds(wildPokemonInfo1)).isNotNull();
    }
}
