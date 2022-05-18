package com.github.vaporrrr.btelinked.commands.minecraft;

import com.github.vaporrrr.btelinked.util.WebsiteUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Sync implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (!commandSender.hasPermission("btel.admin.sync") && !commandSender.isOp()) {
            commandSender.sendMessage(ChatColor.RED + "You do not have permission to use that command.");
            return true;
        }
        WebsiteUtil.linkWebsiteToDiscord();
        commandSender.sendMessage("Synced!");
        return true;
    }
}
