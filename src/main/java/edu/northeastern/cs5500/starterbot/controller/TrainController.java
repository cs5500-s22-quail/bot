package edu.northeastern.cs5500.starterbot.controller;

import edu.northeastern.cs5500.starterbot.model.PokemonInfo;
import edu.northeastern.cs5500.starterbot.model.UserPokemon;
import edu.northeastern.cs5500.starterbot.model.WildPokemon;
import javax.inject.Inject;
import net.dv8tion.jda.api.EmbedBuilder;

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
                                // TODO: change attributes for /fight usage
                                //    +
                                // ivUIBundle(fightPokemonInfo)
                                "HP: "
                                        + fightPokemonInfo.getHp()
                                        + " | Level: "
                                        + fightPokemonInfo.getLevel())
                        .setImage(officialArtworkUrl);
        return embedBuilder;
    }

    //    public void showFightProcess(
    //            ButtonClickEvent event, PokemonInfo fightPokemonInfo, UserPokemon userPokemon) {
    //        battleController.battleUI(fightPokemonInfo, userPokemon.getCarriedPokemon(), event);
    //    }

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
            // TODO: level up the pokemon
            System.out.println("before level up: " + userPokemonInfo.getLevel());
            userPokemonController.levelUp(userPokemon);
            System.out.println("after level up: " + userPokemonInfo.getLevel());
        } else {
            embedBuilder.setDescription(
                    "Your pokemon "
                            + userPokemonInfo.getName()
                            + " lost...Nice try and good luck next time!");
        }
        embedBuilder.setTitle("The fight is over.").setImage(null);

        return embedBuilder;
    }

    public EmbedBuilder getLevelUpEmbeds(EmbedBuilder embedBuilder, PokemonInfo pokemonInfo) {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        //        embedBuilder.setTitle("Your pokemon is level up!").setDescription("Description for
        // ...");
        PokemonInfoController pokemonInfoController = new PokemonInfoController(pokemonInfo);
        embedBuilder = pokemonInfoController.getPokemonInfoEmbed();
        embedBuilder.setTitle(
                "Your pokemon is leveled up!\n\nLevel "
                        + pokemonInfo.getLevel()
                        + " "
                        + pokemonInfo.getName());
        return embedBuilder;
    }
}
