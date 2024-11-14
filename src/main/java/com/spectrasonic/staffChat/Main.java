package com.spectrasonic.staffChat;

import com.spectrasonic.staffChat.Commands.StaffChatCommand;
import com.spectrasonic.staffChat.Utils.MessageUtils;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        MessageUtils.sendStartupMessage(this);
        registerCommands();
    }

    @Override
    public void onDisable() {
        MessageUtils.sendShutdownMessage(this);
    }

    public void registerCommands(){
        Objects.requireNonNull(this.getCommand("sc")).setExecutor(new StaffChatCommand(this));
    }
}
