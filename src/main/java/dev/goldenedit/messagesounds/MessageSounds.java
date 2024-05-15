package dev.goldenedit.messagesounds;

import com.earth2me.essentials.Essentials;
import net.ess3.api.events.PrivateMessageSentEvent;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class MessageSounds extends JavaPlugin implements Listener {
    private FileConfiguration config;
    private Essentials essentials;

    @Override
    public void onEnable() {
        this.config = getConfig();
        config.addDefault("Sound", "BLOCK_NOTE_BLOCK_PLING");
        config.addDefault("MessageSoundsEnabled", true);
        config.options().copyDefaults(true);
        saveConfig();
        this.essentials = (Essentials) getServer().getPluginManager().getPlugin("Essentials");
        if (essentials == null) {
            getLogger().severe("Essentials plugin not found, disabling MessageSounds.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("Successfully loaded in");
    }

    @EventHandler
    public void onMessage(PrivateMessageSentEvent event) {
        if (!config.getBoolean("MessageSoundsEnabled")) return;

        Player recipient = Bukkit.getPlayer(event.getRecipient().getName());
        if (recipient == null) {
            getLogger().warning("Recipient player not found.");
            return;
        }

        String sound = config.getString("Sound");
        try {
            Sound soundEnum = Sound.valueOf(sound);
            recipient.playSound(recipient.getLocation(), soundEnum, 10.0F, 10.0F);
        } catch (IllegalArgumentException e) {
            getLogger().warning("Sound configuration is invalid: " + sound);
        }
    }
}
