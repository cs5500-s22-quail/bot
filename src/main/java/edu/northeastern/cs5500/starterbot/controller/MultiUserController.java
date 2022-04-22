package edu.northeastern.cs5500.starterbot.controller;

import edu.northeastern.cs5500.starterbot.model.PokemonInfo;
import edu.northeastern.cs5500.starterbot.service.PokemonService;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import lombok.Data;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.commands.CommandInteraction;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import net.dv8tion.jda.api.interactions.components.selections.SelectionMenu;

@Data
public class MultiUserController {

    PokemonInfo p1;
    PokemonInfo p2;

    @Inject
    public MultiUserController() {
        this.p1 = new PokemonService().fromID(25);
        this.p2 = new PokemonService().fromID(23);
    }

    public MessageBuilder getMBToSendMessageToUser(CommandInteraction event) {

        // we are supposed to send a message to another user
        MessageBuilder mb = new MessageBuilder();
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Please choose the user you would like to battle with.");
        eb.setImage(
                "https://static.fandomspot.com/images/09/2873/00-featured-battle-anime-pokemon-with-pikachu.jpg");
        List<User> otherUsers = event.getJDA().getUsers();
        List<Member> otherMembers = event.getGuild().getMembers();
        ArrayList<String> otherUsersNames = new ArrayList<>();
        ArrayList<SelectOption> nameOptions = new ArrayList<>();
        for (Member member : otherMembers) {
            String currentName = member.getNickname();
            // if (currentName.toLowerCase().contains("bot")) continue;
            otherUsersNames.add(currentName);
            nameOptions.add(SelectOption.of(currentName, currentName));
        }
        if (nameOptions.size() == 0) {
            eb.setTitle("Sorry there is no friend to play with");
            mb.setEmbeds(eb.build());
            return mb;
        }

        SelectionMenu menu =
                SelectionMenu.create("battleList")
                        .setPlaceholder(
                                "Please choose from the following users to launche a battle.\n")
                        .addOptions(nameOptions)
                        .build();
        mb.setEmbeds(eb.build()).setActionRows(ActionRow.of(menu));

        return mb;
    }

    public void setPokemons(PokemonInfo pokemon1, PokemonInfo pokemon2) {
        this.p1 = pokemon1;
        this.p2 = pokemon2;
    }
}
