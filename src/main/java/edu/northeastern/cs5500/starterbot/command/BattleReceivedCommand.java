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
import org.jetbrains.annotations.NotNull;

@Singleton
@Slf4j
public class BattleReceivedCommand implements ButtonClickHandler {

    @Inject EvenReplyHandler evenReplyHandler;
    @Inject BattleRequestController battleRequestController;
    @Inject UserPokemonController userPokemonController;

    @Inject
    public BattleReceivedCommand() {}

    @Override
    public String getName() {
        return "battle";
    }

    @Override
    public void onButtonClick(@NotNull ButtonClickEvent event) {

        // wait for battle request model updating

        User buttonClicker = event.getUser();

        Date receiveTime = new Date();
        String id = event.getComponentId();
        String handlerName = id.split(":", 2)[1]; // battle:accept

        String receiverUserId = buttonClicker.getId();
        if (handlerName.equals("accept")) {
            BattleRequest battleRequest =
                    battleRequestController.getBattleRequestByReceiverUserId(receiverUserId);

            // case 1 : Other users click the button
            if (battleRequest == null || battleRequest.getInitiatorUserId() == null) {
                event.reply("Sorry, @" + buttonClicker.getName() + " you are not invited!").queue();
                battleRequestController.deleteRequestById(buttonClicker.getId());
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
                battleRequestController.deleteRequestById(buttonClicker.getId());
                return;
            }
            // case 3 : The invitee click the button more than 1 time; (must happen after case 2)
            if (battleRequest.getIsBattle()) {
                event.reply("@" + buttonClicker.getName() + "You already accepted this invitation!")
                        .queue();
                battleRequestController.deleteRequestById(buttonClicker.getId());
                return;
            }
            String initiatorUserId =
                    battleRequestController
                            .getBattleRequestByReceiverUserId(buttonClicker.getId())
                            .getInitiatorUserId();
            PokemonInfo initiatorPokemonInfo =
                    userPokemonController
                            .getUserPokemonForMemberId(initiatorUserId)
                            .getCarriedPokemon();
            PokemonInfo receiverPokemonInfo =
                    userPokemonController
                            .getUserPokemonForMemberId(buttonClicker.getId())
                            .getCarriedPokemon();

            evenReplyHandler.battleUI(initiatorPokemonInfo, receiverPokemonInfo, event);

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

        battleRequestController.deleteRequestById(buttonClicker.getId());
        // if handler is yes then trigger the battle
    }

    public void sendMessage(User receiver, Message message) {
        receiver.openPrivateChannel().flatMap(channel -> channel.sendMessage(message)).queue();
    }
}
