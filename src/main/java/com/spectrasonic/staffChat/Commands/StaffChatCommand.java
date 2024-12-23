package com.spectrasonic.staffChat.Commands;

import com.spectrasonic.staffChat.Main;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class StaffChatCommand implements CommandExecutor, TabCompleter {

    private final Main plugin;
    private final BukkitAudiences adventure;
    private String staffChatPrefix;
    private String messageColor;
    private String usageMessage;
    private String noPermissionMessage;
    private String reloadMessage;
    private String reloadPermissionMessage;

    public StaffChatCommand(Main plugin, BukkitAudiences adventure) {
        this.plugin = plugin;
        this.adventure = adventure;
        loadConfig();
    }

    private void loadConfig() {
        plugin.reloadConfig();
        FileConfiguration config = plugin.getConfig();

        this.staffChatPrefix = config.getString("staff-chat.prefix", "<gray>[</gray><color:#FF0C81><bold>🦋 Nion Staff</bold><gray>]</gray> <gold>» </gold>");
        this.messageColor = config.getString("staff-chat.message-color", "<color:#ff007f>");
        this.usageMessage = config.getString("messages.usage", "<yellow>[❗] Usage: /sc <message> or /sc reload");
        this.noPermissionMessage = config.getString("messages.no-permission", "<red>[❗] You do not have permission to use this command.");
        this.reloadMessage = config.getString("messages.reload-success", "<green>[✓] Configuration reloaded successfully!");
        this.reloadPermissionMessage = config.getString("messages.reload-no-permission", "<red>[❗] You do not have permission to reload the configuration.");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            return false;
        }

        // Manejar comando reload
        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            handleReloadCommand(player);
            return true;
        }

        // Verificar permisos para chat
        if (!player.hasPermission("staffchat.chat")) {
            sendFormattedMessage(player, noPermissionMessage);
            return true;
        }

        // Verificar si hay argumentos
        if (args.length == 0) {
            sendFormattedMessage(player, usageMessage);
            return true;
        }

        // Enviar mensaje al staff
        String message = String.join(" ", args);
        broadcastStaffMessage(player, message);
        return true;
    }

    private void handleReloadCommand(Player player) {
        if (!player.hasPermission("staffchat.reload")) {
            sendFormattedMessage(player, reloadPermissionMessage);
            return;
        }

        try {
            loadConfig();
            sendFormattedMessage(player, reloadMessage);
        } catch (Exception e) {
            sendFormattedMessage(player, "<red>[❌] Error reloading configuration: " + e.getMessage());
            plugin.getLogger().severe("Error reloading configuration: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void broadcastStaffMessage(Player sender, String message) {
        Component formattedMessage = MiniMessage.miniMessage().deserialize(
                staffChatPrefix + messageColor + sender.getName() + ": " + message
        );

        Bukkit.getOnlinePlayers().stream()
                .filter(player -> player.hasPermission("staffchat.chat"))
                .forEach(player -> adventure.player(player).sendMessage(formattedMessage));

        plugin.getLogger().info("StaffChat - " + sender.getName() + ": " + message);
    }

    private void sendFormattedMessage(Player player, String message) {
        adventure.player(player).sendMessage(MiniMessage.miniMessage().deserialize(message));
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (!(sender instanceof Player player)) {
            return completions;
        }

        // Primer argumento
        if (args.length == 1) {
            String input = args[0].toLowerCase();

            // Sugerir 'reload' si tiene permiso
            if (player.hasPermission("staffchat.reload")) {
                if ("reload".startsWith(input)) {
                    completions.add("reload");
                }
            }

            // Agregar sugerencias comunes de mensajes si tiene permiso de chat
            if (player.hasPermission("staffchat.chat")) {
                Stream.of("{MESSAGE}")
                        .filter(msg -> msg.startsWith(input))
                        .forEach(completions::add);
            }
        }

        return completions;
    }
}