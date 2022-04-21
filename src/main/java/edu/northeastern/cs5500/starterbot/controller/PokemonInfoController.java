package edu.northeastern.cs5500.starterbot.controller;

import edu.northeastern.cs5500.starterbot.model.PokemonInfo;
import net.dv8tion.jda.api.EmbedBuilder;

public class PokemonInfoController {

    PokemonInfo pokemonInfo;

    public PokemonInfoController(PokemonInfo pokemonInfo) {
        this.pokemonInfo = pokemonInfo;
    }

    public EmbedBuilder getPokemonInfoEmbed() {
        EmbedBuilder eb = new EmbedBuilder();
        String level = pokemonInfo.getLevel().toString();
        String name = pokemonInfo.getName();

        eb.setTitle("Level " + level + " " + name);
        eb.setDescription(new DisplayController().PokemonInfoUI(pokemonInfo));
        eb.setImage(pokemonInfo.getOfficialArtworkUrl());
        return eb;
    }

}
