package edu.northeastern.cs5500.starterbot.controller;

import edu.northeastern.cs5500.starterbot.model.PokemonIV;
import edu.northeastern.cs5500.starterbot.model.WildPokemon;
import edu.northeastern.cs5500.starterbot.repository.GenericRepository;
import java.util.Collection;
import java.util.Random;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import lombok.Data;

@Data
public class WildPokemonController {
    // maximum id exists in the pokemon API
    private static final int MAX_POKEMON_ID = 898;

    GenericRepository<WildPokemon> wildPokemonRepository;
    Integer randomPokemonID;

    @Inject
    public WildPokemonController(GenericRepository<WildPokemon> wildPokemonRepository) {
        this.wildPokemonRepository = wildPokemonRepository;
        this.setRandomPokemonID();
    }

    public ExtendedPokemon setRandomPokemonID() {
        Random rand = new Random();
        this.randomPokemonID = rand.nextInt(MAX_POKEMON_ID + 1);
        return ExtendedPokemon.fromID(this.randomPokemonID);
    }

    public void updateWildPokemonForChannel(String discordChannelID) {

        String speciesName = ExtendedPokemon.fromID(this.randomPokemonID).getSpeciesName();

        WildPokemon wildPokemon = this.getWildPokemonForChannel(discordChannelID);
        wildPokemon.setWildPokemonName(speciesName);
        wildPokemon.setPokemonIV(this.generatePokemonIV());
        wildPokemonRepository.update(wildPokemon);
    }

    @Nonnull
    public WildPokemon getWildPokemonForChannel(String discordChannelID) {
        Collection<WildPokemon> wildPokemons = wildPokemonRepository.getAll();
        for (WildPokemon wildPokemon : wildPokemons) {
            if (wildPokemon.getDiscordChannel().equals(discordChannelID)) return wildPokemon;
        }

        WildPokemon wildPokemon = new WildPokemon();
        wildPokemon.setDiscordChannel(discordChannelID);

        wildPokemonRepository.add(wildPokemon);
        return wildPokemon;
    }

    @Nonnull
    public PokemonIV generatePokemonIV() {
        PokemonIV pokemonIV = new PokemonIV();
        IndividualValue iv = IndividualValue.generateIV();
        pokemonIV.setAttack(iv.getAttack());
        pokemonIV.setHp(iv.getHp());
        pokemonIV.setDefense(iv.getDefense());
        pokemonIV.setSpecialAttack(iv.getSpecialAttack());
        pokemonIV.setSpecialDefense(iv.getSpecialDefense());
        pokemonIV.setSpeed(iv.getSpeed());
        pokemonIV.setIVPercentage(iv.getIVPercentage());
        pokemonIV.setIVPercentageFormat(iv.getIVPercentageFormat());
        return pokemonIV;
    }
}
