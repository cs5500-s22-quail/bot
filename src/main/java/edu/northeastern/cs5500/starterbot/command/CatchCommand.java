package edu.northeastern.cs5500.starterbot.command;

import edu.northeastern.cs5500.starterbot.controller.UserPokemonController;
import edu.northeastern.cs5500.starterbot.controller.WildPokemonController;
import edu.northeastern.cs5500.starterbot.model.WildPokemon;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.commands.CommandInteraction;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

@Singleton
@Slf4j
public class CatchCommand implements Command {

    @Inject UserPokemonController userPokemonController;
    @Inject WildPokemonController wildPokemonController;

    @Inject
    public CatchCommand() {}

    @Override
    public String getName() {
        return "catch";
    }

    @Override
    public CommandData getCommandData() {
        return new CommandData(getName(), "Spend some coins to try to catch the pokemon");
    }

    @Override
    public void onEvent(CommandInteraction event) {
        log.info("event: /catch");
        String userId = event.getMessageChannel().getId();

        if (!wildPokemonController.hasWildPokemonForChannel(userId)) {
            log.info("catching status: do not have wild pokemon in this channel");
            EmbedBuilder embedBuilder =
                    new EmbedBuilder()
                            .setTitle(
                                    "use /search command to find pokemon in this channel before catching");
            event.replyEmbeds(embedBuilder.build()).queue();
            return;
        }

        WildPokemon wildPokemon = wildPokemonController.getWildPokemonForChannel(userId);
        if (userPokemonController.isPossess(wildPokemon.getPokemonInfo().getName(), userId)) {
            log.info("catching status: User already have this pokemon");
            EmbedBuilder embedBuilder =
                    new EmbedBuilder().setTitle("You already owned this pokemon!");
            event.replyEmbeds(embedBuilder.build()).queue();
            return;
        }
        EmbedBuilder eb =
                new EmbedBuilder()
                        .setTitle("Catching the " + wildPokemon.getPokemonInfo().getName() + "...");

        event.replyEmbeds(eb.build()).queue();

        try {
            Thread.sleep(4000);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        // deduct some coins

        Boolean isCaught =
                userPokemonController.AttemptCatch(
                        wildPokemon.getPokemonInfo().getIv().getQuality());

        if (!isCaught) {
            eb = new EmbedBuilder().setTitle("Oops, the pokemon has ran away. Try another Time.");

        } else {
            eb =
                    new EmbedBuilder()
                            .setTitle("Congratulation! The pokemon has added to your pocket!");
        }
        event.getHook().editOriginalEmbeds(eb.build()).queue();
    }
}
