package de.bloxcrafter.joinLeaveProxyLog.listeners;

import de.bloxcrafter.joinLeaveProxyLog.JoinLeaveProxyLog;
import de.bloxcrafter.joinLeaveProxyLog.util.DiscordWebhookUtil;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class Switch implements Listener {
    private final String webhookUrl = JoinLeaveProxyLog.getInstance().getDiscordWebhookUrl();

    @EventHandler
    public void onSwitch(ServerSwitchEvent event) {
        String player = event.getPlayer().getName();
        String proxy = event.getPlayer().getPendingConnection().getListener().getDefaultServer();
        String fromServer = event.getFrom().getName();
        String toServer = event.getPlayer().getServer().getInfo().getName();
        String group = getPlayerGroup(player);
        String image = "https://crafthead.net/helm/" + player + "/64.png";

        sendEmbed(image, group, player, proxy, fromServer, toServer);
    }

    private String getPlayerGroup(String playerName) {
        LuckPerms luckPerms = LuckPermsProvider.get();
        User user = luckPerms.getUserManager().getUser(playerName);
        if (user != null) {
            return user.getPrimaryGroup();
        }
        return "default";
    }

    private void sendEmbed(String image, String group, String player, String proxy, String fromServer, String toServer) {
        String timestamp = DiscordWebhookUtil.getCurrentTimestamp();
        String jsonPayload = String.format(
                "{\"content\": null, \"embeds\": [{\"title\": \"Jemand hat den Server gewechselt!\", \"description\": \"Gruppe %s Spieler %s ist über Proxy %s von Server %s zu Server %s gewechselt\", \"color\": 16776960, \"footer\": {\"text\": \"SchokiefyNET - Join/Leave Log\", \"icon_url\": \"https://cdn.discordapp.com/attachments/1004149482256093306/1305163875301195776/Schokiefy.Clear.png\"}, \"timestamp\": \"%s\", \"thumbnail\": {\"url\": \"%s\"}}], \"attachments\": []}",
                group, player, proxy, fromServer, toServer, timestamp, image
        );
        DiscordWebhookUtil.sendEmbed(webhookUrl, jsonPayload);
    }
}