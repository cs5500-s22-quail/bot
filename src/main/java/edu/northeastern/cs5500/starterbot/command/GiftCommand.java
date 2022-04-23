package edu.northeastern.cs5500.starterbot.command;

import edu.northeastern.cs5500.starterbot.controller.GiftController;
import edu.northeastern.cs5500.starterbot.controller.UserPokemonController;
import edu.northeastern.cs5500.starterbot.model.PokemonInfo;
import edu.northeastern.cs5500.starterbot.model.UserPokemon;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.SelectionMenuEvent;
import net.dv8tion.jda.api.interactions.commands.CommandInteraction;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

@Singleton
@Slf4j
public class GiftCommand implements Command, SelectionMenuHandler {

    @Inject GiftController giftController;
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
                                        OptionType.STRING,
                                        "receiver",
                                        "Please type in the user name you would like to gift to.")
                                .setRequired(true));
    }

    @Override
    public void onEvent(CommandInteraction event) {
        String receiver = event.getOption("receiver").getAsString();
        List<Member> members = event.getGuild().getMembers();
        String receiverId = "";
        for (Member member : members) {
            if (member.getUser().getName().equals(receiver)) receiverId = member.getId();
        }

        if (receiverId.length() == 0) {
            event.reply("Your user name input is incorrect!");
            return;
        }

        log.info("event: /gift");
        String discordUserId = event.getUser().getId();
        MessageBuilder mb =
                giftController.getPokemonList(
                        discordUserId, event.getUser().getName(), receiver, receiverId);
        Message message = mb.build();
        event.reply(message).queue();
    }

    @Override
    public void onSelectionMenu(SelectionMenuEvent event) {
        String chosenPokemonName = event.getInteraction().getValues().get(0);
        String senderId = event.getUser().getId();

        log.info("onGiftSelectionMenu", event.getComponent().getId());
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
