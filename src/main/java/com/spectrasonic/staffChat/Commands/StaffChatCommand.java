package com.spectrasonic.staffChat.Commands;

import com.spectrasonic.staffChat.Main;
import net.md_5.bungee.api.ChatColor;
import com.spectrasonic.staffChat.Utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;

public class StaffChatCommand implements CommandExecutor {

    public StaffChatCommand(Main plugin) {
    }
    @SuppressWarnings("all")
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (player.hasPermission("staffchat.chat") || player.isOp()) {
                if (args.length > 0) {
                    StringBuilder message = new StringBuilder();
                    for (String arg : args) {
                        message.append(arg).append(" ");
                    }
                    String SCPREFIX = ChatColor.translateAlternateColorCodes('&', "&7[&dStaff&7] &6» &b");
                    String formattedMessage = SCPREFIX + message.toString();
                    Bukkit.getOnlinePlayers().stream()
                            .filter(p -> p.hasPermission("staffchat.chat"))
                            .forEach(p -> p.sendMessage(formattedMessage));
                } else {
                    player.sendMessage(net.md_5.bungee.api.ChatColor.of("#FFFF") + "[❗] Usage: /sc <sessage>");

                }
            } else {
                MessageUtils.sendMessage(sender, "&e[❗] You do not have permission to use this command.");
                player.sendMessage(net.md_5.bungee.api.ChatColor.of("#FFFF") + "[❗] You do not have permission to use this command.");
            }
            return true;
        }
        return false;
    }
}
