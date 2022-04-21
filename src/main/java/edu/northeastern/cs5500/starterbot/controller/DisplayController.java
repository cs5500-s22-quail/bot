package edu.northeastern.cs5500.starterbot.controller;

import edu.northeastern.cs5500.starterbot.model.PokemonInfo;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

@Singleton
@Slf4j
public class DisplayController {

    @Inject
    public DisplayController() {}

    public String PokemonInfoUI(PokemonInfo pokemonInfo) {
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

    private String ivUI(String statName, int baseStat, int iv) {
        return System.lineSeparator() + statName + ": " + baseStat + " - IV: " + iv + "/31";
    }
}
