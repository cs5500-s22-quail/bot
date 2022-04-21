package edu.northeastern.cs5500.starterbot.command;

import edu.northeastern.cs5500.starterbot.controller.PokemonGenerator;
import edu.northeastern.cs5500.starterbot.controller.TrainController;
import edu.northeastern.cs5500.starterbot.controller.UserPokemonController;
import edu.northeastern.cs5500.starterbot.model.PokemonInfo;
import edu.northeastern.cs5500.starterbot.model.UserPokemon;
import edu.northeastern.cs5500.starterbot.model.WildPokemon;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.interactions.commands.CommandInteraction;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.components.Button;

@Singleton
@Slf4j
public class BattleCommand implements Command, ButtonClickHandler {
    //    @Inject EmbedBuilderGenerator embedBuilderGenerator;
    @Inject UserPokemonController userPokemonController;
    @Inject PokemonGenerator pokemonGenerator;
    @Inject TrainController trainController;

    @Inject
    public BattleCommand() {}

    @Override
    public String getName() {
        return "battle";
    }

    @Override
    public CommandData getCommandData() {
        return new CommandData(getName(), "Train your pokemon walking with you");
    }

    @Override
    public void onEvent(CommandInteraction event) {
        log.info("event: /battle");
        event.reply("How do you want to level up your pokemon?")
                .addActionRow(
                        Button.primary("train:battle", "Battle"), // Button with only a label
                        Button.secondary("train:decline", "Decline")
                                .withEmoji(Emoji.fromUnicode("U+2694")))
                .queue();
    }

    @Override
    public void onButtonClick(ButtonClickEvent event) {
        if (event.getComponentId().equals("train:cancel")) {
            event.reply("Canceled the request...").queue(); // TODO: finishing...
        } else if (event.getComponentId().equals("train:battle")) {

            MessageBuilder mb = new MessageBuilder();
            EmbedBuilder eb1 = new EmbedBuilder();
            EmbedBuilder eb2 = new EmbedBuilder();

            WildPokemon fightPokemon = pokemonGenerator.getWildPokemon();
            String userId = event.getUser().getId();
            //            TrainController trainController = new TrainController(userId);
            EmbedBuilder embedBuilder = trainController.getFightPokemonEmbeds(fightPokemon);
            // TODO: for now, event ends here by showing the wild pokemon image
            // Once changes for saving and calling existing pokemon are done,
            // needs to add logics for comparing level and showing the results
            event.replyEmbeds(embedBuilder.build()).queue();

            UserPokemon userPokemon = userPokemonController.getUserPokemonForMemberID(userId);
            PokemonInfo userPokemonInfo = userPokemon.getCarriedPokemon();
            int levelBefore = userPokemonInfo.getLevel();
            embedBuilder =
                    trainController.getFightResultEmbeds(
                            embedBuilder, fightPokemon.getPokemonInfo(), userPokemon);
            event.getHook().editOriginalEmbeds(embedBuilder.build()).queue();
            int levelAfter = userPokemonInfo.getLevel();
            if (levelBefore < levelAfter) {
                embedBuilder = trainController.getLevelUpEmbeds(embedBuilder, userPokemonInfo);
                event.getHook().editOriginalEmbeds(embedBuilder.build()).queue();
            }
        }
    }
}
