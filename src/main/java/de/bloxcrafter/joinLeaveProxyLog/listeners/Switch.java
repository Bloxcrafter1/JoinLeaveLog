package de.bloxcrafter.joinLeaveProxyLog.listeners;

import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.driver.service.ServiceInfoSnapshot;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import de.bloxcrafter.joinLeaveProxyLog.JoinLeaveProxyLog;
import de.bloxcrafter.joinLeaveProxyLog.util.DiscordWebhookUtil;

public class Switch implements Listener {

    private String webhookUrl = JoinLeaveProxyLog.getInstance().getDiscordWebhookUrl();

    @EventHandler
    public void onSwitch(ServerSwitchEvent event) {
        String player = event.getPlayer().getName();
        if (Join.hasJustJoined(player)) {
            return; // Skip if the player has just joined
        }
        String proxy = getProxyName();
        String fromServer = "Unknown";
        if (event.getFrom() != null) {
            fromServer = getServerName(event.getFrom().getName());
        }
        String toServer = getServerName(event.getPlayer().getServer().getInfo().getName());
        String image = "https://crafthead.net/helm/" + player + "/64.png";

        sendEmbed(image, player, proxy, fromServer, toServer);
    }

    private String getServerName(String serverName) {
        ServiceInfoSnapshot serviceInfoSnapshot = CloudNetDriver.getInstance().getCloudServiceProvider().getCloudServiceByName(serverName);
        return serviceInfoSnapshot != null ? serviceInfoSnapshot.getServiceId().getName() : "DefaultServer";
    }

    private String getProxyName() {
        ServiceInfoSnapshot serviceInfoSnapshot = CloudNetDriver.getInstance().getCloudServiceProvider().getCloudServiceByName(JoinLeaveProxyLog.getInstance().getProxy().getName());
        return serviceInfoSnapshot != null ? serviceInfoSnapshot.getServiceId().getName() : "DefaultProxy";
    }

    public void sendEmbed(String image, String player, String proxy, String fromServer, String toServer) {
        String timestamp = DiscordWebhookUtil.getCurrentTimestamp();
        String jsonPayload = String.format(
                "{\"content\": null, \"embeds\": [{\"title\": \"Jemand hat den Server gewechselt!\", \"description\": \"Spieler %s ist über Proxy %s von Server %s zu Server %s gewechselt\", \"color\": 16776960, \"footer\": {\"text\": \"SchokiefyNET - Join/Leave Log\", \"icon_url\": \"https://cdn.discordapp.com/attachments/1004149482256093306/1305163875301195776/Schokiefy.Clear.png\"}, \"timestamp\": \"%s\", \"thumbnail\": {\"url\": \"%s\"}}], \"attachments\": []}",
                player, proxy, fromServer, toServer, timestamp, image
        );
        DiscordWebhookUtil.sendEmbed(webhookUrl, jsonPayload);
    }
}