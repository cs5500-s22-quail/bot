package edu.northeastern.cs5500.starterbot.command;

import edu.northeastern.cs5500.starterbot.controller.*;
import edu.northeastern.cs5500.starterbot.model.BattleRequest;
import edu.northeastern.cs5500.starterbot.model.PokemonInfo;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;

@Singleton
@Slf4j
public class BattleReceivedCommand implements ButtonClickHandler {

    @Inject BattleController battleController;
    @Inject MultiUserController multiUserController;
    @Inject BattleRequestController battleRequestController;
    @Inject UserPokemonController userPokemonController;
    // @Override
    // public void onPrivateMessageReceived(@NotNull PrivateMessageReceivedEvent event) {
    //     String id = event.getMessageId();
    //     String originalSender = id.split(":", 3)[1];
    //     String reply = id.split(":", 3)[2];

    //     // check if the receiver is the original sender.
    //     String eventAuthor = event.getAuthor().getName();
    //     if(eventAuthor.equals(originalSender)) {
    //         // launch the battle if the reply is true;

    //         if(reply.equals("true")) {

    //             event.getJDA().getEventManager().handle(event);

    //             // send the message back to the orginal user

    //         } else {
    //             // notify the user that the invitation has been declined.
    //         }

    //         return;
    //     }

    //     // if not, launch the invitation.
    // }

    @Inject
    public BattleReceivedCommand() {}

    @Override
    public String getName() {
        return "battle";
    }

    @Override
    public void onButtonClick(ButtonClickEvent event) {

        // wait for battle request model updating
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        User buttonClicker = event.getUser();

        Date receiveTime = new Date();
        String id = event.getComponentId();
        String handlerName = id.split(":", 2)[1]; // battle:accept

        String receiverUserId = buttonClicker.getId();
        if (handlerName.equals("accept")) {
            BattleRequest battleRequest =
                    battleRequestController.getBattleRequestByReceiverUserId(receiverUserId);

            // case 1 : Other users click the button
            if (battleRequest == null) {
                event.reply("Sorry, @" + buttonClicker.getName() + " you are not been invited!")
                        .queue();
                return;
            }

            Date requestTime = battleRequest.getDate();

            long diff = receiveTime.getTime() - requestTime.getTime();

            long seconds = TimeUnit.MILLISECONDS.toSeconds(diff);
            log.info("request time stamp is" + requestTime);
            log.info("received time stamp is" + receiveTime);
            log.info(seconds + " seconds has been passed");

            // case 2 :The request has expired
            if (seconds > 60) {
                event.reply("The request initiated in " + requestTime + " is no longer valid!")
                        .queue();
                return;
            }
            // case 3 : The invitee click the button more than 1 time; (must happen after case 2)
            if (battleRequest.getIsBattle()) {
                event.reply(
                                "@"
                                        + buttonClicker.getName()
                                        + "Your already accepted this invitation!")
                        .queue();
                return;
            }
            String initiatorUserId =
                    battleRequestController
                            .getBattleRequestByReceiverUserId(buttonClicker.getId())
                            .getInitiatorUserId();
            PokemonInfo initiatorPokemonInfo =
                    userPokemonController
                            .getUserPokemonForMemberID(initiatorUserId)
                            .getCarriedPokemon();
            PokemonInfo receiverPokemonInfo =
                    userPokemonController
                            .getUserPokemonForMemberID(buttonClicker.getId())
                            .getCarriedPokemon();
            battleController.battleUI(initiatorPokemonInfo, receiverPokemonInfo, event);

            battleRequestController.setHasBattled(receiverUserId);

        } else {
            // send a message back to indicate decline
            MessageBuilder mb = new MessageBuilder();
            EmbedBuilder eb = new EmbedBuilder();
            eb.setTitle(
                    "Sorry, your invitation has been declined. \n"
                            + "\nYou could try another user.");
            mb.setEmbeds(eb.build());
        }
        // if handler is yes then trigger the battle
    }

    public void sendMessage(User receiver, Message message) {
        receiver.openPrivateChannel().flatMap(channel -> channel.sendMessage(message)).queue();
    }
}
