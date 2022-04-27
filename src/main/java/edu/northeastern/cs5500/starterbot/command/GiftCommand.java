package edu.northeastern.cs5500.starterbot.command;

import edu.northeastern.cs5500.starterbot.controller.UserPokemonController;
import edu.northeastern.cs5500.starterbot.model.PokemonInfo;
import edu.northeastern.cs5500.starterbot.model.UserPokemon;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SelectionMenuEvent;
import net.dv8tion.jda.api.interactions.commands.CommandInteraction;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import net.dv8tion.jda.api.interactions.components.selections.SelectionMenu;

@Singleton
@Slf4j
public class GiftCommand implements Command, SelectionMenuHandler {

    @Inject UserPokemonController userPokemonController;

    @Inject
    public GiftCommand() {}

    @Override
    public String getName() {
        return "gift";
    }

    @Override
    public CommandData getCommandData() {
        return new CommandData(getName(), "Gift a pokemon to one of the users.")
                .addOptions(
                        new OptionData(
                                        OptionType.USER,
                                        "receiver",
                                        "Please chooose one of the users to gift.")
                                .setRequired(true));
    }

    @Override
    public void onEvent(CommandInteraction event) {
        User receiverUser = event.getOption("receiver").getAsUser();
        String receiver = receiverUser.getName();
        List<Member> members = event.getGuild().getMembers();
        String receiverId = "";
        for (Member member : members) {
            if (member.getUser().getName().equals(receiver)) receiverId = member.getUser().getId();
        }

        if (receiverId.length() == 0) {
            event.reply("Your user name input is incorrect!").queue();
            return;
        }

        log.info("event: /gift");
        String discordUserId = event.getUser().getId();
        MessageBuilder mb = new MessageBuilder();

        UserPokemon userPokemon = userPokemonController.getUserPokemonForMemberID(discordUserId);

        EmbedBuilder eb = new EmbedBuilder();

        eb.setTitle("Please choose one your pokemons to gift to " + receiver)
                .setDescription(
                        "\n\nPlease note that you can not give your carried pokemon to others");
        eb.setImage("https://c.tenor.com/JdW7qW5GGMcAAAAM/christmas-pokemon.gif");
        ArrayList<PokemonInfo> pokemons = userPokemon.getPokemonTeam();
        ArrayList<SelectOption> nameOptions = new ArrayList<>();
        Set<String> pokemonSet = new HashSet<>();
        for (PokemonInfo pokemon : pokemons) {
            // could not give carried pokemon to others.
            // pokemon with duplicates names will only have the first pokemon that shows up
            if (pokemon.getName().equals(userPokemon.getCarriedPokemon().getName())) continue;
            String currentName = pokemon.getName();
            if (pokemonSet.contains(currentName)) continue;
            pokemonSet.add(currentName);
            nameOptions.add(SelectOption.of(currentName, currentName));
        }

        if (nameOptions.size() == 0) {
            eb.setTitle("Sorry You do not have any pokemons to give");
            mb.setEmbeds(eb.build());
        } else {

            SelectionMenu menu =
                    SelectionMenu.create("gift-" + discordUserId)
                            .setPlaceholder(
                                    "Please choose one from the following pokemons to give\n")
                            .addOptions(nameOptions)
                            .build();
            mb.setEmbeds(eb.build()).setActionRows(ActionRow.of(menu));
        }

        Message message = mb.build();

        event.reply(message).queue();
    }

    @Override
    public void onSelectionMenu(SelectionMenuEvent event) {
        String chosenPokemonName = event.getInteraction().getValues().get(0);
        String senderId = event.getUser().getId();

        String receiverId = event.getComponent().getId().split("-", 2)[1];
        UserPokemon receiverUserPokemon =
                userPokemonController.getUserPokemonForMemberID(receiverId);

        UserPokemon senderUserPokemon = userPokemonController.getUserPokemonForMemberID(senderId);

        ArrayList<PokemonInfo> senderPokemons = senderUserPokemon.getPokemonTeam();

        PokemonInfo chosenPokemonInfo = null;
        for (PokemonInfo pokemonInfo : senderPokemons) {
            if (pokemonInfo.getName().equals(chosenPokemonName)) {
                chosenPokemonInfo = pokemonInfo;
                break;
            }
        }

        if (chosenPokemonInfo == null) {
            event.reply("There is something wrong with the pokemon chosen. Try again!").queue();
            return;
        }

        // if matched one, give it to the receiver
        senderPokemons.remove(chosenPokemonInfo);
        receiverUserPokemon.getPokemonTeam().add(chosenPokemonInfo);

        // update the repository
        userPokemonController.updateUserPokemon(senderUserPokemon);
        userPokemonController.updateUserPokemon(receiverUserPokemon);

        event.reply("Your pokemon " + chosenPokemonName + " has been gifted!").queue();
    }
}
