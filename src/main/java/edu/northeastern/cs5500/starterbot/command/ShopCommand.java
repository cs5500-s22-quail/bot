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
public class ShopCommand implements Command {

    @Inject UserPreferenceController userPreferenceController;
    @Inject ShopController shopController;

    @Inject
    public ShopCommand() {}

    @Override
    public String getName() {
        return "shop";
    }

    @Override
    public CommandData getCommandData() {
        return new CommandData(
                getName(), "Provide the balance of the user and instructions for further steps.");
    }

    @Override
    public void onEvent(CommandInteraction event) {
        log.info("event: /shop");

        String discordUserId = event.getUser().getId();
        String preferredName = userPreferenceController.getPreferredNameForUser(discordUserId);

        event.reply(shopController.getBalanceEB(discordUserId, preferredName).build()).queue();
    }
}
