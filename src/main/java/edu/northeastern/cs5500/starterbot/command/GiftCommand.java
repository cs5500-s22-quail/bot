package edu.northeastern.cs5500.starterbot.command;

import edu.northeastern.cs5500.starterbot.controller.GiftController;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.events.interaction.SelectionMenuEvent;
import net.dv8tion.jda.api.interactions.commands.CommandInteraction;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

@Singleton
@Slf4j
public class GiftCommand implements Command, SelectionMenuHandler {

    @Inject GiftController giftController;

    @Inject
    public GiftCommand() {}

    @Override
    public String getName() {
        return "gift";
    }

    @Override
    public CommandData getCommandData() {
        return new CommandData(getName(), "Gift a pokemon to one of the users.");
    }

    @Override
    public void onEvent(CommandInteraction event) {
        log.info("event: /gift");
        String discordUserId = event.getUser().getId();
        MessageBuilder mb = giftController.getPokemonList(discordUserId, event.getUser().getName());
        event.reply(mb.build()).queue();
    }

    @Override
    public void onSelectionMenu(SelectionMenuEvent event) {
        //

    }
}
