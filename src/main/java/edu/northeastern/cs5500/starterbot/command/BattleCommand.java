package edu.northeastern.cs5500.starterbot.command;

import edu.northeastern.cs5500.starterbot.controller.*;
import edu.northeastern.cs5500.starterbot.model.PokemonInfo;
import edu.northeastern.cs5500.starterbot.service.PokemonService;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.interactions.commands.CommandInteraction;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.components.Button;

@Singleton
@Slf4j
public class BattleCommand implements Command, ButtonClickHandler {

    @Inject UserPokemonController userPokemonController;
    @Inject PokemonGenerator pokemonGenerator;
    @Inject TrainController trainController;
    @Inject PokemonService pokemonService;
    @Inject MultiUserController multiUserController;
    @Inject DisplayController displayController;
    @Inject BattleController battleController;

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
        String userName = "Unknown";
        event.reply(userName + " want to battle with you. ")
                .addActionRow(
                        Button.primary("battle:pk", "Battle")
                                .withEmoji(Emoji.fromUnicode("U+2694")), // Button with only a label
                        Button.secondary("battle:decline", "Decline"))
                .queue();
    }

    @Override
    public void onButtonClick(ButtonClickEvent event) {
        if (event.getComponentId().equals("battle:decline")) {
            event.reply("Canceled the request...").queue(); // TODO: finishing...
        } else if (event.getComponentId().equals("battle:pk")) {

            PokemonInfo p1 = multiUserController.getP1();
            PokemonInfo p2 = multiUserController.getP2();

            battleController.battleUI(p1, p2, event);
        }
    }
}
