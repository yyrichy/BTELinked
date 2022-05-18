package com.github.vaporrrr.btelinked;

import com.github.vaporrrr.btelinked.commands.minecraft.Reload;
import com.github.vaporrrr.btelinked.commands.minecraft.Sync;
import com.github.vaporrrr.btelinked.listeners.DiscordListener;
import de.leonhard.storage.Config;
import de.leonhard.storage.LightningBuilder;
import de.leonhard.storage.internal.settings.ConfigSettings;
import de.leonhard.storage.internal.settings.DataType;
import de.leonhard.storage.internal.settings.ReloadSettings;
import github.scarsz.discordsrv.DiscordSRV;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.InputStream;

public class BTELinked extends JavaPlugin {
    private final Config config;
    private RoleUpdater roleUpdater = new RoleUpdater();

    public BTELinked() {
        super();
        InputStream is = getClassLoader().getResourceAsStream("config.yml");
        config = LightningBuilder
                .fromFile(new File(getDataFolder(), "config.yml"))
                .addInputStream(is)
                .setDataType(DataType.SORTED)
                .setConfigSettings(ConfigSettings.PRESERVE_COMMENTS)
                .setReloadSettings(ReloadSettings.MANUALLY)
                .createConfig()
                .addDefaultsFromInputStream(is);
    }

    @Override
    public void onEnable() {
        getLogger().info("BTELinked Enabled!");
        getCommand("btel-reload").setExecutor(new Reload());
        getCommand("btel-sync").setExecutor(new Sync());
        DiscordSRV.api.subscribe(new DiscordListener());
    }

    public static BTELinked getPlugin() {
        return getPlugin(BTELinked.class);
    }

    public static Config config() {
        return getPlugin().config;
    }

    public void reloadConfig() {
        config().forceReload();
    }

    public void reload() {
        reloadConfig();
        startRoleUpdater();
    }

    public void startRoleUpdater() {
        if(!roleUpdater.isInterrupted()) roleUpdater.interrupt();
        if (config().getInt("WebsiteDiscordLink.IntervalInMins") > 0) {
            roleUpdater = new RoleUpdater();
            roleUpdater.start();
        }
    }

    public static void info(String message) {
        getPlugin().getLogger().info(message);
    }

    public static void warn(String message) {
        getPlugin().getLogger().warning(message);
    }

    public static void severe(String message) {
        getPlugin().getLogger().severe(message);
    }
}
