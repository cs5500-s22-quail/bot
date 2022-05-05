package edu.northeastern.cs5500.starterbot.command;

import edu.northeastern.cs5500.starterbot.controller.*;
import edu.northeastern.cs5500.starterbot.model.PokemonInfo;
import edu.northeastern.cs5500.starterbot.model.UserPokemon;
import edu.northeastern.cs5500.starterbot.service.PokemonService;
import java.util.ArrayList;
import java.util.List;
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
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import net.dv8tion.jda.api.interactions.components.selections.SelectionMenu;

@Singleton
@Slf4j
public class BattleCommand implements Command, SelectionMenuHandler {

    @Inject UserPokemonController userPokemonController;
    @Inject PokemonGenerator pokemonGenerator;
    @Inject TrainController trainController;
    @Inject PokemonService pokemonService;
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
        MessageBuilder mb = getMBToSendMessageToUser(event);
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
                userPokemonController.getUserPokemonForMemberId(event.getUser().getId());
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

        battleRequestController.deleteRequestById(receiver.getId());
        battleRequestController.setBattleRequestByUserId(receiver.getId(), initiator.getId());

        Message message = mb.build();
        event.reply(message).queue();
    }

    public void sendMessage(User receiver, Message message) {
        receiver.openPrivateChannel().flatMap(channel -> channel.sendMessage(message)).queue();

        //           .delay(30, TimeUnit.SECONDS) // RestAction<Message> with
        //                // delayed response
        //                .flatMap(Message::delete)
    }

    private MessageBuilder getMBToSendMessageToUser(CommandInteraction event) {

        // we are supposed to send a message to another user
        MessageBuilder mb = new MessageBuilder();
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Please choose the user you would like to battle with.");
        eb.setImage(
                "https://static.fandomspot.com/images/09/2873/00-featured-battle-anime-pokemon-with-pikachu.jpg");
        // user userPokemon controller to get all the users.
        ArrayList<SelectOption> nameOptions = new ArrayList<>();
        // ArrayList<String> usersIdList =
        // userPokemonController.getUsersList(event.getUser().getId());
        List<Member> members = event.getGuild().getMembers();
        for (Member member : members) {
            // User user = event.getJDA().retrieveUserById(id).complete();
            String currentName = member.getUser().getName();
            if (currentName.equals(event.getUser().getName())
                    || currentName.toLowerCase().contains("bot")) continue;
            nameOptions.add(SelectOption.of(currentName, currentName));
        }
        if (nameOptions.size() == 0) {
            eb.setTitle("Sorry there is no friend to play with");
            mb.setEmbeds(eb.build());
            return mb;
        }

        SelectionMenu menu =
                SelectionMenu.create("battle")
                        .setPlaceholder(
                                "Please choose from the following users to launche a battle.\n")
                        .addOptions(nameOptions)
                        .build();
        mb.setEmbeds(eb.build()).setActionRows(ActionRow.of(menu));

        return mb;
    }
}
