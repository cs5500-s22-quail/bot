package edu.northeastern.cs5500.starterbot.controller;

import edu.northeastern.cs5500.starterbot.model.PokemonInfo;
import edu.northeastern.cs5500.starterbot.model.UserPokemon;
import edu.northeastern.cs5500.starterbot.model.WildPokemon;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.dv8tion.jda.api.EmbedBuilder;

@Singleton
public class TrainController {

    @Inject BattleController battleController;

    UserPokemonController userPokemonController;

    @Inject
    public TrainController(UserPokemonController userPokemonController) {
        this.userPokemonController = userPokemonController;
    }

    public EmbedBuilder getFightPokemonEmbeds(WildPokemon fightPokemon) {
        PokemonInfo fightPokemonInfo = fightPokemon.getPokemonInfo();
        String officialArtworkUrl = fightPokemonInfo.getOfficialArtworkUrl();

        EmbedBuilder embedBuilder =
                new EmbedBuilder()
                        .setTitle(fightPokemonInfo.getName() + " is fighting with your pokemon...")
                        .setDescription(
                                "HP: "
                                        + fightPokemonInfo.getHp()
                                        + " | Level: "
                                        + fightPokemonInfo.getLevel())
                        .setImage(officialArtworkUrl);
        return embedBuilder;
    }

    public EmbedBuilder getFightResultEmbeds(
            EmbedBuilder embedBuilder, PokemonInfo fightPokemonInfo, UserPokemon userPokemon) {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        BattleController battleController = new BattleController();
        PokemonInfo userPokemonInfo = userPokemon.getCarriedPokemon();
        Object[] battleRes =
                battleController.pokemonVersusPokemon(fightPokemonInfo, userPokemonInfo);
        PokemonInfo winnerInfo = (PokemonInfo) battleRes[0];
        boolean userWin = winnerInfo == userPokemonInfo;
        if (userWin) {
            embedBuilder.setDescription("Your pokemon " + userPokemonInfo.getName() + " win!");
            userPokemonController.levelUp(userPokemon);
        } else {
            embedBuilder.setDescription(
                    "Your pokemon "
                            + userPokemonInfo.getName()
                            + " lost...Nice try and good luck next time!");
        }
        embedBuilder.setTitle("The fight is over.").setImage(null);

        return embedBuilder;
    }

    public EmbedBuilder getLevelUpEmbeds(PokemonInfo pokemonInfo) {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        PokemonInfoController pokemonInfoController = new PokemonInfoController(pokemonInfo);
        EmbedBuilder embedBuilder = pokemonInfoController.getPokemonInfoEmbed();
        embedBuilder.setTitle(
                "Your pokemon is leveled up!\n\nLevel "
                        + pokemonInfo.getLevel()
                        + " "
                        + pokemonInfo.getName());
        return embedBuilder;
    }
}
