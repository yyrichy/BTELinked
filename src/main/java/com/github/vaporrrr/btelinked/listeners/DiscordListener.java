package com.github.vaporrrr.btelinked.listeners;

import com.github.vaporrrr.btelinked.BTELinked;
import com.github.vaporrrr.btelinked.util.WebsiteUtil;
import de.leonhard.storage.Config;
import github.scarsz.discordsrv.api.ListenerPriority;
import github.scarsz.discordsrv.api.Subscribe;
import github.scarsz.discordsrv.api.events.DiscordGuildMessageReceivedEvent;
import github.scarsz.discordsrv.dependencies.jda.api.events.guild.member.GuildMemberJoinEvent;

public class DiscordListener {
    @Subscribe
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        if (BTELinked.config().getBoolean("WebsiteDiscordLink.OnMemberJoin")) {
            WebsiteUtil.linkWebsiteToDiscord();
        }
    }

    @Subscribe(priority = ListenerPriority.MONITOR)
    public void discordMessageReceived(DiscordGuildMessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;

        if (!event.getMessage().getContentRaw().startsWith("&")) return;

        Config config = BTELinked.config();
        if (event.getMessage().getContentRaw().equals("&sync")) {
            if (config.getStringList("DiscordCommands.Sync.Permissions.Roles").stream().anyMatch(s -> event.getMember().getRoles().stream().anyMatch(r -> r.getId().equals(s))) || config.getStringList("DiscordCommands.Sync.Permissions.Users").stream().anyMatch(u -> event.getAuthor().getId().equals(u))) {
                WebsiteUtil.linkWebsiteToDiscord();
                event.getChannel().sendMessage("Synced!").queue();
            }
        }
    }
}
