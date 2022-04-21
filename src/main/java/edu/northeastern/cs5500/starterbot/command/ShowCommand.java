package edu.northeastern.cs5500.starterbot.command;

import edu.northeastern.cs5500.starterbot.controller.ShopController;
import edu.northeastern.cs5500.starterbot.controller.UserPreferenceController;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.interactions.commands.CommandInteraction;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

@Singleton
@Slf4j
public class ShowCommand implements Command {

    @Inject UserPreferenceController userPreferenceController;
    @Inject ShopController shopController;

    @Inject
    public ShowCommand() {}

    @Override
    public String getName() {
        return "show";
    }

    @Override
    public CommandData getCommandData() {
        return new CommandData(
                getName(), "You could redeem your balance for the following moves and pokemons");
    }

    @Override
    public void onEvent(CommandInteraction event) {
        log.info("event: /show");

        String discordUserId = event.getUser().getId();
        String preferredName = userPreferenceController.getPreferredNameForUser(discordUserId);
        String discordChannelId = event.getChannel().getId();

        event.reply(
                        shopController
                                .getShowEB(discordUserId, preferredName, discordChannelId)
                                .build())
                .queue();
    }
}
