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
    @Inject BattleController battleController;
    @Inject BattleRequestController battleRequestController;

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
        User initiator = event.getUser();
        User receiver = event.getJDA().getUsers().get(0);
        for (User user : event.getJDA().getUsers()) {
            if (user.getName().equals(chosenUser)) {
                receiver = user;
                break;
            }
        }

        MessageBuilder mb = new MessageBuilder();
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle(initiator.getName() + " sent you a battle invitation with this pokemon!")
                .setDescription("Please choose from the following buttons")
                .setAuthor("@" + receiver.getName());

        UserPokemon userPokemon =
                userPokemonController.getUserPokemonForMemberID(event.getUser().getId());
        PokemonInfo userPokeInfo = userPokemon.getCarriedPokemon();
        if (userPokeInfo == null) {
            event.reply("You do not have any pokemon yet. Go get a pokemon").queue();
            return;
        }
        EmbedBuilder eb2 = displayController.pokemonStatus(userPokeInfo);
        mb.setEmbeds(eb.build(), eb2.build())
                .setActionRows(
                        ActionRow.of(
                                Button.primary("battle:accept", "accept"),
                                Button.secondary("battle:decline", "decline")));

        // setBattle
        battleRequestController.setBattleRequestByUserId(receiver.getId(), initiator.getId());

        Message message = mb.build();
        event.reply(message).queue();

        //        int count = 0;
        //        while (count < 30) {
        //            if (!battleRequest.getAcceptEventId().equals("Invalid")) {
        //
        //                PokemonInfo receiverPokemonInfo =
        //                        userPokemonController
        //                                .getUserPokemonForMemberID(receiver.getId(),
        // receiver.getName())
        //                                .getCarriedPokemon();
        //
        //                battleController.battleUI(userPokeInfo, receiverPokemonInfo, event);
        //                battleRequestController.deleteRequestById(
        //                        event.getId(), event.getGuild().getId(),
        // event.getMessageChannel().getId());
        //                return;
        //            }
        //            try {
        //                Thread.sleep(1000);
        //            } catch (InterruptedException ex) {
        //                Thread.currentThread().interrupt();
        //            }
        //            count++;
        //        }

        //        MessageBuilder mb1 = new MessageBuilder();
        //        EmbedBuilder eb1 = new EmbedBuilder();
        //        eb1.setTitle(
        //                receiver.getName()
        //                        + "did not respond to your invitation. This request has been
        // canceled");
        //        event.reply(mb1.setEmbeds(eb1.build()).build()).queue();

        // if (event.getComponentId().equals("battle:decline")) {

        // } else if (event.getComponentId().equals("battle:pk")) {

        //     PokemonInfo p1 = multiUserController.getP1();
        //     PokemonInfo p2 = multiUserController.getP2();

        //     battleController.battleUI(p1, p2, event);
        // }

    }

    public void sendMessage(User receiver, Message message) {
        receiver.openPrivateChannel().flatMap(channel -> channel.sendMessage(message)).queue();

        //           .delay(30, TimeUnit.SECONDS) // RestAction<Message> with
        //                // delayed response
        //                .flatMap(Message::delete)
    }
}
