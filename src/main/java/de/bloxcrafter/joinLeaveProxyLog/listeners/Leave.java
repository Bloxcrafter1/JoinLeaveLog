package de.bloxcrafter.joinLeaveProxyLog.listeners;

import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import de.bloxcrafter.joinLeaveProxyLog.JoinLeaveProxyLog;
import de.bloxcrafter.joinLeaveProxyLog.util.DiscordWebhookUtil;

public class Leave implements Listener {

    private String webhookUrl = JoinLeaveProxyLog.getInstance().getDiscordWebhookUrl();

    @EventHandler
    public void onLeave(PlayerDisconnectEvent event) {
        String player = event.getPlayer().getName();
        String proxy = event.getPlayer().getPendingConnection().getVirtualHost().getHostString();
        String server = event.getPlayer().getPendingConnection().getListener().getDefaultServer();

        sendEmbed(player, proxy, server);
    }

    public void sendEmbed(String player, String proxy, String server) {
        String timestamp = DiscordWebhookUtil.getCurrentTimestamp();
        String jsonPayload = String.format(
                "{\"content\": null, \"embeds\": [{\"title\": \"Jemand hat verlassen!\", \"description\": \"Spieler %s hat das SchokiefyNET Netzwerk verlassen\", \"color\": 16711680, \"footer\": {\"text\": \"SchokiefyNET - Join/Leave Log\", \"icon_url\": \"https://cdn.discordapp.com/attachments/1004149482256093306/1305163875301195776/Schokiefy.Clear.png?ex=67320814&is=6730b694&hm=3e0cd4e1115fbd9406f3bb8d4f7496fcd5fd3135ac83f7882237af810ee98dd8&\"}, \"timestamp\": \"%s\"}], \"attachments\": []}",
                player, timestamp
        );
        DiscordWebhookUtil.sendEmbed(webhookUrl, jsonPayload);
    }
}