package com.github.vaporrrr.btelinked.commands;

import com.github.vaporrrr.btelinked.BTELinked;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Reload implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (!commandSender.hasPermission("btel.admin.reload") && !commandSender.isOp()) {
            commandSender.sendMessage(ChatColor.RED + "You do not have permission to use that command.");
            return true;
        }
        BTELinked.getPlugin().reload();
        commandSender.sendMessage("Plugin reloaded!");
        return true;
    }
}
