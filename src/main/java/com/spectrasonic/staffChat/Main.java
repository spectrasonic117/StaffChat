package com.spectrasonic.staffChat;

import com.spectrasonic.staffChat.Commands.StaffChatCommand;
import com.spectrasonic.staffChat.Utils.MessageUtils;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;
import java.io.File;


public class Main extends JavaPlugin {

    private BukkitAudiences adventure;

    @Override
    public void onEnable() {

        this.adventure = BukkitAudiences.create(this);

        registerCommands();
        setupConfig();

        saveDefaultConfig();
        reloadConfig();
        MessageUtils.sendStartupMessage(this);

    }

    @Override
    public void onDisable() {
        this.adventure = null;
        saveConfig();
        MessageUtils.sendShutdownMessage(this);
    }

    public void registerCommands(){
        Objects.requireNonNull(this.getCommand("sc")).setExecutor(new StaffChatCommand(this, this.adventure));
    }

    public BukkitAudiences adventure() {
        if(this.adventure == null) {
            throw new IllegalStateException("Tried to access Adventure when the plugin was disabled!");
        }
        return this.adventure;
    }

    private void setupConfig() {
        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }

        File configFile = new File(getDataFolder(), "config.yml");

        if (!configFile.exists()) {
            saveDefaultConfig();
            getLogger().info("Configuration file created successfully!");
        }

        reloadConfig();

    }
}
