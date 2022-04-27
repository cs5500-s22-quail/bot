package edu.northeastern.cs5500.starterbot.command;

import edu.northeastern.cs5500.starterbot.controller.ShopController;
import edu.northeastern.cs5500.starterbot.controller.UserPokemonController;
import edu.northeastern.cs5500.starterbot.controller.UserPreferenceController;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.interactions.commands.CommandInteraction;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

@Singleton
@Slf4j
public class ProfileCommand implements Command {

    @Inject UserPreferenceController userPreferenceController;
    @Inject ShopController shopController;
    @Inject UserPokemonController userPokemonController;

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

        MessageBuilder mb = new MessageBuilder();
        EmbedBuilder eb = getProfile(discordUserId, discordAvatarUrl, preferredName);
        mb.setEmbeds(eb.build());

        event.reply(mb.build()).queue();
    }

    private EmbedBuilder getProfile(
            String discordUserId, String discordAvatarUrl, String preferredName) {
        int balance =
                shopController
                        .getBalanceForUserId(discordUserId)
                        .getBalance(); // the balance, initially set to 0. TBD
        int pokemons =
                userPokemonController
                        .getUserPokemonForMemberID(discordUserId)
                        .getPokemonTeam()
                        .size(); // the captured pokemons, initially set to 0. TBD
        EmbedBuilder eb =
                new EmbedBuilder()
                        .setTitle(preferredName + "'s Profile")
                        .setDescription(
                                "Balance: "
                                        + String.valueOf(balance)
                                        + "\nCaptured Pokemons: "
                                        + String.valueOf(pokemons))
                        .setThumbnail(discordAvatarUrl);

        return eb;
    }
}
