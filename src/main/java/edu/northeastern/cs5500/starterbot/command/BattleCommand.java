package edu.northeastern.cs5500.starterbot.command;

import edu.northeastern.cs5500.starterbot.controller.*;
import edu.northeastern.cs5500.starterbot.model.PokemonInfo;
import edu.northeastern.cs5500.starterbot.model.UserPokemon;
import edu.northeastern.cs5500.starterbot.service.PokemonService;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SelectionMenuEvent;
import net.dv8tion.jda.api.interactions.commands.CommandInteraction;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Button;

@Singleton
@Slf4j
public class BattleCommand implements Command, SelectionMenuHandler {

    @Inject UserPokemonController userPokemonController;
    @Inject PokemonGenerator pokemonGenerator;
    @Inject TrainController trainController;
    @Inject PokemonService pokemonService;
    @Inject MultiUserController multiUserController;
    @Inject DisplayController displayController;

    @Inject
    public BattleCommand() {}

    @Override
    public String getName() {
        return "battle";
    }

    @Override
    public CommandData getCommandData() {
        return new CommandData(getName(), "Train your pokemon walking with you");
    }

    @Override
    public void onEvent(CommandInteraction event) {
        log.info("event: /battle");
        MessageBuilder mb = multiUserController.getMBToSendMessageToUser(event);
        event.reply(mb.build()).queue();
    }

    @Override
    public void onSelectionMenu(SelectionMenuEvent event) {
        String chosenUser = event.getInteraction().getValues().get(0);
        User receiver = event.getJDA().getUsers().get(0);
        for (User user : event.getJDA().getUsers()) {
            if (user.getName().equals(chosenUser)) {
                receiver = user;
                break;
            }
        }

        MessageBuilder mb = new MessageBuilder();
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle(event.getUser().getName() + " sent you a battle invitation with this pokemon!")
                .setDescription("Please choose from the following buttons")
                .setAuthor(event.getUser().getName());

        UserPokemon userPokemon =
                userPokemonController.getUserPokemonForMemberID(
                        event.getUser().getId(), event.getUser().getName());
        PokemonInfo userPokeInfo = userPokemon.getCarriedPokemon();

        EmbedBuilder eb2 = displayController.pokemonStatus(userPokeInfo);
        mb.setEmbeds(eb.build(), eb2.build())
                .setActionRows(
                        ActionRow.of(
                                Button.primary("battle:accept", "accept"),
                                Button.secondary("battle:decline", "decline")));

        sendMessage(receiver, mb.build());

        MessageBuilder mb1 = new MessageBuilder();
        EmbedBuilder eb1 = new EmbedBuilder();
        eb1.setTitle("You invitation has been sent. Please wait for reply.");
        event.reply(mb1.setEmbeds(eb1.build()).build()).queue();

        // if (event.getComponentId().equals("battle:decline")) {

        // } else if (event.getComponentId().equals("battle:pk")) {

        //     PokemonInfo p1 = multiUserController.getP1();
        //     PokemonInfo p2 = multiUserController.getP2();

        //     battleController.battleUI(p1, p2, event);
        // }

    }

    public void sendMessage(User receiver, Message message) {
        receiver.openPrivateChannel().flatMap(channel -> channel.sendMessage(message)).queue();
    }
}
