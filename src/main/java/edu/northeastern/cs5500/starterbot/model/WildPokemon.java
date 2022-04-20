package edu.northeastern.cs5500.starterbot.model;

import lombok.Data;
import org.bson.types.ObjectId;

@Data
public class WildPokemon implements Model {
    ObjectId id;

    PokemonInfo pokemonInfo;

    String discordChannel;
}
