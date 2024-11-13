package de.bloxcrafter.joinLeaveProxyLog.listeners;

import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.driver.service.ServiceInfoSnapshot;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import de.bloxcrafter.joinLeaveProxyLog.JoinLeaveProxyLog;
import de.bloxcrafter.joinLeaveProxyLog.util.DiscordWebhookUtil;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class Join implements Listener {

    private String webhookUrl = JoinLeaveProxyLog.getInstance().getDiscordWebhookUrl();
    private static final Set<String> justJoinedPlayers = new HashSet<>();

    @EventHandler
    public void onJoin(PostLoginEvent event) {
        String player = event.getPlayer().getName();
        String proxy = getProxyName();
        String server = getServerName(event.getPlayer().getServer().getInfo().getName());
        String image = "https://crafthead.net/helm/" + player + "/64.png";

        justJoinedPlayers.add(player);
        sendEmbed(image, player, proxy, server);

        // Remove player from justJoinedPlayers after 5 seconds
        JoinLeaveProxyLog.getInstance().getProxy().getScheduler().schedule(JoinLeaveProxyLog.getInstance(), () -> {
            justJoinedPlayers.remove(player);
        }, 5, TimeUnit.SECONDS);
    }

    private String getServerName(String serverName) {
        ServiceInfoSnapshot serviceInfoSnapshot = CloudNetDriver.getInstance().getCloudServiceProvider().getCloudServiceByName(serverName);
        return serviceInfoSnapshot != null ? serviceInfoSnapshot.getServiceId().getName() : "DefaultServer";
    }

    private String getProxyName() {
        ServiceInfoSnapshot serviceInfoSnapshot = CloudNetDriver.getInstance().getCloudServiceProvider().getCloudServiceByName(JoinLeaveProxyLog.getInstance().getProxy().getName());
        return serviceInfoSnapshot != null ? serviceInfoSnapshot.getServiceId().getName() : "DefaultProxy";
    }

    public void sendEmbed(String image, String player, String proxy, String server) {
        String timestamp = DiscordWebhookUtil.getCurrentTimestamp();
        String jsonPayload = String.format(
                "{\"content\": null, \"embeds\": [{\"title\": \"Jemand ist gejoint!\", \"description\": \"Spieler %s ist über Proxy %s auf Server %s gejoint\", \"color\": 4062976, \"footer\": {\"text\": \"SchokiefyNET - Join/Leave Log\", \"icon_url\": \"https://cdn.discordapp.com/attachments/1004149482256093306/1305163875301195776/Schokiefy.Clear.png\"}, \"timestamp\": \"%s\", \"thumbnail\": {\"url\": \"%s\"}}], \"attachments\": []}",
                player, proxy, server, timestamp, image
        );
        DiscordWebhookUtil.sendEmbed(webhookUrl, jsonPayload);
    }

    public static boolean hasJustJoined(String player) {
        return justJoinedPlayers.remove(player);
    }
}