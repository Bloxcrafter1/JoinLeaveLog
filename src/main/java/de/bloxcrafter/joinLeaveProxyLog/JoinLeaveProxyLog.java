package de.bloxcrafter.joinLeaveProxyLog;

import de.bloxcrafter.joinLeaveProxyLog.commands.ReloadCommand;
import de.bloxcrafter.joinLeaveProxyLog.listeners.Join;
import de.bloxcrafter.joinLeaveProxyLog.listeners.Leave;
import de.bloxcrafter.joinLeaveProxyLog.listeners.Switch;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public final class JoinLeaveProxyLog extends Plugin {

    // This is the instance of the Plugin
    private static JoinLeaveProxyLog instance;

    // String of Discord WEBHOOK URL
    private String discordWebhookUrl;

    // This is for the Config
    private Configuration config;


    // This is for Get Discord WEBHOOK URL
    public String getDiscordWebhookUrl() {
        return discordWebhookUrl;
    }

    // This is for the Configuration File (DON'T REMOVE)
    public static JoinLeaveProxyLog getInstance() {
        return instance;
    }


    @Override
    public void onEnable() {
        // Plugin startup logic

        // This is for the Instance
        instance = this;

        // This is for the Config
        loadConfig();

        // Started Message
        getLogger().info("|#######################");
        getLogger().info("|");
        getLogger().info("| Join Leave Log started up...!");
        getLogger().info("| Version: 2.0.0");
        getLogger().info("| Author: Bloxcrafter");
        getLogger().info("|");
        getLogger().info("|#######################");

        // Get Discord WEBHOOK URL
        discordWebhookUrl = config.getString("discord.webhook-url");

        // Check if Discord WEBHOOK URL is not empty and if its empty than you get a Warning
        if (discordWebhookUrl != null && discordWebhookUrl.isEmpty()) {
            getLogger().log(Level.INFO, "|#######################");
            getLogger().log(Level.INFO, "|");
            getLogger().log(Level.INFO, "|Discord Webhook URL loaded successfully: " + discordWebhookUrl + "");
            getLogger().log(Level.INFO, "|");
            getLogger().log(Level.INFO, "|#######################");
        } else {
            getLogger().log(Level.WARNING, "|#######################");
            getLogger().log(Level.WARNING, "|");
            getLogger().log(Level.WARNING, "|Discord Webhook URL not found or empty in config.yml! You can enter it and when your finsihed use ingame the command /joinleavelog reload!");
            getLogger().log(Level.WARNING, "|");
            getLogger().log(Level.WARNING, "|#######################");
        }

        // Register the Listeners
        PluginManager pm = getProxy().getPluginManager();
        pm.registerListener(this, new Join());
        pm.registerListener(this, new Leave());
        pm.registerListener(this, new Switch());

        // Register the Command
        getProxy().getPluginManager().registerCommand(this, new ReloadCommand());

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

        // Shuted down Message
        getLogger().info("|#######################");
        getLogger().info("|");
        getLogger().info("| Join Leave Log shuted down...!");
        getLogger().info("| Version: 2.0.0");
        getLogger().info("| Author: Bloxcrafter");
        getLogger().info("|");
        getLogger().info("|#######################");
    }

    // This is the Load Config Method
    private void loadConfig() {
        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }

        File configFile = new File(getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
                config = getConfig();
                config.set("discord.webhook-url", "https://discord.com/api/webhooks/YOUR_WEBHOOK_URL_HERE");
                saveConfig(config);
            } catch (IOException e) {
                getLogger().log(Level.SEVERE, "|#######################");
                getLogger().log(Level.SEVERE, "|");
                getLogger().log(Level.SEVERE, "|Could not create config.yml!" , e);
                getLogger().log(Level.SEVERE, "|");
                getLogger().log(Level.SEVERE, "|#######################");
            }
        } else {
            config = getConfig();
        }
    }

    // This is the Get Config Method
    private Configuration getConfig() {
        try {
            return ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(getDataFolder(), "config.yml"));
        } catch (IOException e) {
            getLogger().log(Level.SEVERE, "Could not load config.yml!", e);
            return null;
        }
    }

    // This is the Save Config Method
    private void saveConfig(Configuration config) {
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, new File(getDataFolder(), "config.yml"));
        } catch (IOException e) {
            getLogger().log(Level.SEVERE, "Could not save config.yml!", e);
        }
    }

    // This is the Reload Config Method
    public void reloadPluginConfig() {
        loadConfig();
        discordWebhookUrl = config.getString("discord.webhook-url");
    }
}
