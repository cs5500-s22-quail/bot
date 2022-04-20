package edu.northeastern.cs5500.starterbot.command;

import edu.northeastern.cs5500.starterbot.controller.ShopController;
import edu.northeastern.cs5500.starterbot.controller.UserPreferenceController;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.interaction.SelectionMenuEvent;
import net.dv8tion.jda.api.interactions.commands.CommandInteraction;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

@Singleton
@Slf4j
public class RedeemCommand implements Command, SelectionMenuHandler {

    @Inject UserPreferenceController userPreferenceController;
    @Inject ShopController shopController;

    @Inject
    public RedeemCommand() {}

    @Override
    public String getName() {
        return "redeem";
    }

    @Override
    public CommandData getCommandData() {
        return new CommandData(
                getName(), "Please choose a pokemon from the dropdown menu to make a purchase.");
    }

    @Override
    public void onEvent(CommandInteraction event) {
        log.info("event: /redeem");
        String discordUserId = event.getUser().getId();
        String preferredName = userPreferenceController.getPreferredNameForUser(discordUserId);
        String discordChannelId = event.getChannel().getId();
        event.reply(
                        shopController
                                .getRedeemEB(discordUserId, preferredName, discordChannelId)
                                .build())
                .queue();
    }

    @Override
    public void onSelectionMenu(SelectionMenuEvent event) {
        event.reply(event.getInteraction().getValues().get(0)).queue();
    }
}
