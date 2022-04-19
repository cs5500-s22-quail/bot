package edu.northeastern.cs5500.starterbot.listener;

import edu.northeastern.cs5500.starterbot.command.Command;
import edu.northeastern.cs5500.starterbot.controller.ExtendedPokemon;
import edu.northeastern.cs5500.starterbot.controller.WildPokemonController;
import edu.northeastern.cs5500.starterbot.model.WildPokemon;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import javax.inject.Inject;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public class MessageListener extends ListenerAdapter {
    @Inject WildPokemonController wildPokemonController;

    @Inject Set<Command> commands;

    @Inject
    public MessageListener() {
        super();
    }

    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        for (Command command : commands) {
            if (command.getName().equals(event.getName())) {
                command.onEvent(event);
                return;
            }
        }
    }

    public Collection<CommandData> allCommandData() {
        return commands.stream().map(Command::getCommandData).collect(Collectors.toList());
    }

    @Override
    public void onButtonClick(ButtonClickEvent event) {
        if (event.getComponentId().equals("hello")) {
            event.reply("Hello :)").queue();
        } else if (event.getComponentId().equals("emoji")) {
//            event.reply("xixixi :)");
            //            log.info("event: /search");
            EmbedBuilder embedBuilder = generateWildPokemonEmbeds(event);

            try {
                event.wait(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // TODO: for now, event ends here by showing the wild pokemon image
            // Once changes for saving and calling existing pokemon are done,
            // needs to add logics for comparing level and showing the results

            /*
            dummy code:
            if(
             */
            event.replyEmbeds(embedBuilder.build()).queue();
        }
    }

    //    private WildPokemon getWildPokemon(ButtonClickEvent event) {
    //
    //    }

    private EmbedBuilder generateWildPokemonEmbeds(ButtonClickEvent event) {
        String channelID = event.getMessageChannel().getId();
        ExtendedPokemon extendedPokemon = wildPokemonController.setRandomPokemonID();
        wildPokemonController.updateWildPokemonForChannel(channelID);

        String officialArtworkUrl = extendedPokemon.getOfficialArtworkUrl();
        WildPokemon wildPokemon = wildPokemonController.getWildPokemonForChannel(channelID);

        EmbedBuilder embedBuilder =
                new EmbedBuilder()
                        .setTitle("A wild pokemon has appeared!")
                        .setDescription(
                                extendedPokemon.getSpeciesName()
                                        + "is fighting with your pokemon."
                                        + "\n"
                                        // TODO: change attributes for /fight usage
                                        + ivUI(
                                                "HP",
                                                extendedPokemon.getStatHp(),
                                                wildPokemon.getPokemonIV().getHp())
                                        + ivUI(
                                                "Attack",
                                                extendedPokemon.getStatAttack(),
                                                wildPokemon.getPokemonIV().getAttack())
                                        + ivUI(
                                                "Defense",
                                                extendedPokemon.getStatDefense(),
                                                wildPokemon.getPokemonIV().getDefense())
                                        + ivUI(
                                                "Sp.Atk",
                                                extendedPokemon.getStatSpecialAttack(),
                                                wildPokemon.getPokemonIV().getSpecialAttack())
                                        + ivUI(
                                                "Sp.Def",
                                                extendedPokemon.getStatSpecialDefense(),
                                                wildPokemon.getPokemonIV().getSpecialDefense())
                                        + ivUI(
                                                "Speed",
                                                extendedPokemon.getStatSpeed(),
                                                wildPokemon.getPokemonIV().getSpeed())
                                        + "\nTotal IV: "
                                        + wildPokemon.getPokemonIV().getIVPercentageFormat())
                        .setImage(officialArtworkUrl);

        return embedBuilder;
    }

    private String ivUI(String statName, int baseStat, int iv) {
        return "\n" + statName + ": " + baseStat + " - IV: " + iv + "/31";
    }
}
