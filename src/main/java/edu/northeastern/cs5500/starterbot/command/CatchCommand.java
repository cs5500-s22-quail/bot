package edu.northeastern.cs5500.starterbot.command;

import edu.northeastern.cs5500.starterbot.controller.ShopController;
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
    @Inject ShopController shopController;

    private static final int COST_PER_CATCH = 1;

    @Inject
    public CatchCommand() {}

    @Override
    public String getName() {
        return "catch";
    }

    @Override
    public CommandData getCommandData() {
        return new CommandData(getName(), "This will cost you " + COST_PER_CATCH + "coins.");
    }

    @Override
    public void onEvent(CommandInteraction event) {
        log.info("event: /catch");
        String channelId = event.getMessageChannel().getId();

        if (!wildPokemonController.hasWildPokemonForChannel(channelId)) {
            log.info("catching status: do not have wild pokemon in this channel");
            EmbedBuilder embedBuilder =
                    new EmbedBuilder()
                            .setTitle(
                                    "use /search command to find pokemon in this channel before catching");
            event.replyEmbeds(embedBuilder.build()).queue();
            return;
        }
        String userId = event.getUser().getId();

        WildPokemon wildPokemon = wildPokemonController.getWildPokemonForChannel(channelId);
        if (userPokemonController.isPossess(wildPokemon.getPokemonInfo().getName(), userId)) {
            log.info("catching status: User already have this pokemon");
            EmbedBuilder embedBuilder =
                    new EmbedBuilder().setTitle("You already owned this pokemon!");
            event.replyEmbeds(embedBuilder.build()).queue();
            return;
        }

        EmbedBuilder eb =
                new EmbedBuilder()
                        .setTitle("Catching the " + wildPokemon.getPokemonInfo().getName() + "...")
                        .setDescription("-" + COST_PER_CATCH + " coins, Your Current Balance is ");

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
            userPokemonController.addPokemon(wildPokemon.getPokemonInfo(), userId);
            wildPokemonController.deletePokemonInfoForChannel(channelId);
        }
        event.getHook().editOriginalEmbeds(eb.build()).queue();
    }
}
