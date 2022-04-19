package edu.northeastern.cs5500.starterbot.listener;

import edu.northeastern.cs5500.starterbot.command.Command;
import edu.northeastern.cs5500.starterbot.controller.PokemonGenerator;
import edu.northeastern.cs5500.starterbot.controller.WildPokemonController;
import edu.northeastern.cs5500.starterbot.model.PokemonInfo;
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
    @Inject PokemonGenerator pokemonGenerator;

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
        if (event.getComponentId().equals("moneyMagic")) {
            event.reply("MoneyMagic feature is developing...").queue();
        } else if (event.getComponentId().equals("fight")) {
            EmbedBuilder embedBuilder = generateWildPokemonEmbeds(event);
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

    //TODO: move to somewhere else
    private EmbedBuilder generateWildPokemonEmbeds() {
        WildPokemon fightPokemon = pokemonGenerator.getWildPokemon();
        PokemonInfo fightPokemonInfo = fightPokemon.getPokemonInfo();
        String officialArtworkUrl = fightPokemonInfo.getOfficialArtworkUrl();

        EmbedBuilder embedBuilder =
                new EmbedBuilder()
                        .setTitle(fightPokemonInfo.getName() + " is fighting with your pokemon...")
                        .setDescription(
                                // TODO: change attributes for /fight usage
                                //                                        +
                                // ivUIBundle(fightPokemonInfo)
                                "HP: "
                                        + fightPokemonInfo.getHp()
                                        + " | Level: "
                                        + fightPokemonInfo.getLevel())
                        .setImage(officialArtworkUrl);

        return embedBuilder;
    }

    private String ivUI(String statName, int baseStat, int iv) {
        return "\n" + statName + ": " + baseStat + " - IV: " + iv + "/31";
    }
}
