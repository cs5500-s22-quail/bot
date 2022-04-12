package edu.northeastern.cs5500.starterbot.command;

import edu.northeastern.cs5500.starterbot.controller.ProfileController;
import edu.northeastern.cs5500.starterbot.controller.UserPreferenceController;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.interactions.commands.CommandInteraction;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

@Singleton
@Slf4j
public class ProfileCommand implements Command {

    @Inject UserPreferenceController userPreferenceController;
    @Inject ProfileController profileController;

    @Inject
    public ProfileCommand() {}

    @Override
    public String getName() {
        return "profile";
    }

    @Override
    public CommandData getCommandData() {
        return new CommandData(getName(), "Provide the up-to-date history of your activities");
    }

    @Override
    public void onEvent(CommandInteraction event) {
        log.info("event: /profile");

        String discordUserId = event.getUser().getId();
        String discordAvatarUrl = event.getUser().getAvatarUrl();
        String preferredName = userPreferenceController.getPreferredNameForUser(discordUserId);

        event.replyEmbeds(
                        profileController
                                .getProfile(discordUserId, discordAvatarUrl, preferredName)
                                .build())
                .queue();
    }
}
