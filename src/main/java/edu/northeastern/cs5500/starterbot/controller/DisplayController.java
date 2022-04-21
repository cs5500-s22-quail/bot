package edu.northeastern.cs5500.starterbot.controller;

import edu.northeastern.cs5500.starterbot.model.PokemonInfo;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;

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

    public EmbedBuilder pokemonStatus(PokemonInfo pokemonInfo) {
        EmbedBuilder currentEmbedBuilder = new EmbedBuilder();
        // for each pokemon, the price of it is a random number plus
        // 100 of balance.

        Quality quality = pokemonInfo.getIv().getQuality();
        String pokemonQuality = "";
        switch (quality) {
            case RED:
                currentEmbedBuilder = currentEmbedBuilder.setColor(0xff210d);
                pokemonQuality = "Legendary";
                break;
            case PURPLE:
                currentEmbedBuilder = currentEmbedBuilder.setColor(0xc30dff);
                pokemonQuality = "Epic";
                break;
            case BLUE:
                currentEmbedBuilder = currentEmbedBuilder.setColor(0x0d7eff);
                pokemonQuality = "Superior";
                break;
            case GREEN:
                currentEmbedBuilder = currentEmbedBuilder.setColor(0x0dff82);
                pokemonQuality = "Good";
                break;
            default:
                break;
        }
        currentEmbedBuilder.setTitle(
                "\n" + "HP: " + pokemonInfo.getHp() + "/" + pokemonInfo.getHp());

        String sb =
                pokemonInfo.getName()
                        + "\n"
                        + "Attack: "
                        + pokemonInfo.getAttack()
                        + "\n"
                        + "Defense: "
                        + pokemonInfo.getDefense()
                        + "\n"
                        + "Total IV "
                        + pokemonInfo.getIv().getIVPercentageFormat();

        currentEmbedBuilder.setDescription(sb);
        currentEmbedBuilder.setThumbnail(pokemonInfo.getOfficialArtworkUrl());

        return currentEmbedBuilder;
    }
}
