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
        eb.setDescription(this.ivUIBundle());
        eb.setImage(pokemonInfo.getOfficialArtworkUrl());
        return eb;
    }

    public String ivUIBundle() {
        return ivUI("HP", pokemonInfo.getHp(), pokemonInfo.getIv().getHp())
                + ivUI("Attack", pokemonInfo.getAttack(), pokemonInfo.getIv().getAttack())
                + ivUI("Defense", pokemonInfo.getDefense(), pokemonInfo.getIv().getDefense())
                + ivUI(
                        "Sp.Atk",
                        pokemonInfo.getSpecialAttack(),
                        pokemonInfo.getIv().getSpecialAttack())
                + ivUI(
                        "Sp.Def",
                        pokemonInfo.getSpecialDefense(),
                        pokemonInfo.getIv().getSpecialDefense())
                + ivUI("Speed", pokemonInfo.getSpeed(), pokemonInfo.getIv().getSpeed())
                + System.lineSeparator()
                + "Total IV: "
                + pokemonInfo.getIv().getIVPercentageFormat();
    }

    public String ivUI(String statName, int baseStat, int iv) {
        return System.lineSeparator() + statName + ": " + baseStat + " - IV: " + iv + "/31";
    }
}
