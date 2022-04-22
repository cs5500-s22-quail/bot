package edu.northeastern.cs5500.starterbot.command;

import edu.northeastern.cs5500.starterbot.controller.BattleController;
import edu.northeastern.cs5500.starterbot.controller.MultiUserController;
import edu.northeastern.cs5500.starterbot.model.PokemonInfo;
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
        String id = event.getButton().getId();
        String handlerName = id.split(":", 2)[1];
        String userIdToSend = handlerName.split("-", 2)[1];

        User userToSend = event.getJDA().getUserById(userIdToSend);

        if (handlerName.equals("accept")) {

            PokemonInfo p1 = multiUserController.getP1();
            PokemonInfo p2 = multiUserController.getP2();

            Message toSend = battleController.battleUI(p1, p2, event);

            event.reply(toSend);

            sendMessage(userToSend, toSend);

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
