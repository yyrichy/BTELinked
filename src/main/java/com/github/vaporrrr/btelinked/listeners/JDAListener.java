package com.github.vaporrrr.btelinked.listeners;

import com.github.vaporrrr.btelinked.BTELinked;
import com.github.vaporrrr.btelinked.util.WebsiteUtil;
import github.scarsz.discordsrv.dependencies.jda.api.events.guild.member.GuildMemberJoinEvent;
import github.scarsz.discordsrv.dependencies.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class JDAListener extends ListenerAdapter {
    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        if (BTELinked.config().getBoolean("WebsiteDiscordLink.OnMemberJoin")) {
            WebsiteUtil.linkWebsiteToDiscord();
        }
    }
}
