package edu.northeastern.cs5500.starterbot;

import dagger.Component;
import edu.northeastern.cs5500.starterbot.command.CommandModule;
import edu.northeastern.cs5500.starterbot.listener.MessageListener;
import edu.northeastern.cs5500.starterbot.repository.RepositoryModule;
import java.util.EnumSet;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.security.auth.login.LoginException;
import lombok.SneakyThrows;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

@Component(modules = {CommandModule.class, RepositoryModule.class})
@Singleton
interface BotComponent {
    public Bot bot();
}

public class Bot {

    @Inject
    Bot() {}

    @Inject MessageListener messageListener;

    static String getBotToken() {
        return new ProcessBuilder().environment().get("BOT_TOKEN");
    }

    @SneakyThrows(LoginException.class)
    void start() {
        String token = getBotToken();
        if (token == null) {
            throw new IllegalArgumentException(
                    "The BOT_TOKEN environment variable is not defined.");
        }
        JDA jda =
                JDABuilder.createLight(token, EnumSet.noneOf(GatewayIntent.class))
                        .setChunkingFilter(
                                ChunkingFilter.ALL) // enable member chunking for all guilds
                        .setMemberCachePolicy(MemberCachePolicy.ALL) // ignored if chunking enabled
                        .enableIntents(GatewayIntent.GUILD_MEMBERS)
                        .addEventListeners(messageListener)
                        .build();

        CommandListUpdateAction commands = jda.updateCommands();
        commands.addCommands(messageListener.allCommandData());
        commands.queue();

        List<Guild> guilds = jda.getGuilds();
        for (Guild guild : guilds) {
            CommandListUpdateAction updateGuild = guild.updateCommands();
            updateGuild.addCommands(messageListener.allCommandData());
            updateGuild.queue();
        }
    }
}
