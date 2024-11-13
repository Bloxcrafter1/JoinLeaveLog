package de.bloxcrafter.joinLeaveProxyLog;

import de.bloxcrafter.joinLeaveProxyLog.listeners.Join;
import de.bloxcrafter.joinLeaveProxyLog.listeners.Leave;
import de.bloxcrafter.joinLeaveProxyLog.listeners.Switch;
import de.bloxcrafter.joinLeaveProxyLog.util.DiscordWebhookUtil;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public final class JoinLeaveProxyLog extends Plugin {

    private static JoinLeaveProxyLog instance;
    private Configuration config;
    private String discordWebhookUrl;

    @Override
    public void onEnable() {
        instance = this;
        loadConfig();

        getLogger().info("|#######################");
        getLogger().info("| Schokiefy Log Plugin enabled! ;D");
        getLogger().info("| Version: 1.0.14");
        getLogger().info("|#######################");

        discordWebhookUrl = config.getString("discord.webhook-url");

        if (discordWebhookUrl == null || discordWebhookUrl.isEmpty()) {
            getLogger().log(Level.WARNING, "Discord Webhook URL not found or empty in config.yml!");
        } else {
            getLogger().log(Level.INFO, "Discord Webhook URL loaded successfully: " + discordWebhookUrl);
            sendStartupEmbed(getProxy().getName());
        }

        PluginManager pm = getProxy().getPluginManager();
        pm.registerListener(this, new Join());
        pm.registerListener(this, new Leave());
        pm.registerListener(this, new Switch());
    }

    @Override
    public void onDisable() {
        sendShutdownEmbed(getProxy().getName());

        instance = null;
        getLogger().info("|#######################");
        getLogger().info("| Schokiefy Log Plugin disabled! :(");
        getLogger().info("| Version: 1.0.14");
        getLogger().info("|#######################");
    }

    public static JoinLeaveProxyLog getInstance() {
        return instance;
    }

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
                getLogger().log(Level.SEVERE, "Could not create config.yml!", e);
            }
        } else {
            config = getConfig();
        }
    }

    private Configuration getConfig() {
        try {
            return ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(getDataFolder(), "config.yml"));
        } catch (IOException e) {
            getLogger().log(Level.SEVERE, "Could not load config.yml!", e);
            return null;
        }
    }

    private void saveConfig(Configuration config) {
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, new File(getDataFolder(), "config.yml"));
        } catch (IOException e) {
            getLogger().log(Level.SEVERE, "Could not save config.yml!", e);
        }
    }

    public String getDiscordWebhookUrl() {
        return discordWebhookUrl;
    }

    public void sendStartupEmbed(String proxy) {
        String timestamp = DiscordWebhookUtil.getCurrentTimestamp();
        String jsonPayload = String.format(
                "{\"content\": null, \"embeds\": [{\"title\": \"Proxy Hochgefahren!\", \"description\": \"Die Proxy %s ist hochgefahren und kann absofort Spieler empfangen!\", \"color\": 4062976, \"footer\": {\"text\": \"SchokiefyNET - Proxy Log\", \"icon_url\": \"https://cdn.discordapp.com/attachments/1004149482256093306/1305163875301195776/Schokiefy.Clear.png\"}, \"timestamp\": \"%s\"}], \"attachments\": []}",
                proxy, timestamp
        );
        DiscordWebhookUtil.sendEmbed(discordWebhookUrl, jsonPayload);
    }

    public void sendShutdownEmbed(String proxy) {
        String timestamp = DiscordWebhookUtil.getCurrentTimestamp();
        String jsonPayload = String.format(
                "{\"content\": null, \"embeds\": [{\"title\": \"Proxy Heruntergefahren!\", \"description\": \"Die Proxy %s ist heruntergefahren und kann absofort keine Spieler empfangen.\", \"color\": 16711680, \"footer\": {\"text\": \"SchokiefyNET - Proxy Log\", \"icon_url\": \"https://cdn.discordapp.com/attachments/1004149482256093306/1305163875301195776/Schokiefy.Clear.png\"}, \"timestamp\": \"%s\"}], \"attachments\": []}",
                proxy, timestamp
        );
        DiscordWebhookUtil.sendEmbed(discordWebhookUrl, jsonPayload);
    }
}