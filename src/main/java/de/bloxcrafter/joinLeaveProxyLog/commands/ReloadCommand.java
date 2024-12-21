package de.bloxcrafter.joinLeaveProxyLog.commands;

import de.bloxcrafter.joinLeaveProxyLog.JoinLeaveProxyLog;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class ReloadCommand extends Command {

    public ReloadCommand() {
        super("reloadconfig", "joinleaveproxylog.reload", "rlconfig");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        JoinLeaveProxyLog plugin = JoinLeaveProxyLog.getInstance();
        if (sender.hasPermission("joinleaveproxylog.reload")) {
            try {
                plugin.reloadPluginConfig();
                String reloadMessage = plugin.getPluginConfig().getString("messages.reloadmessage");
                sender.sendMessage(reloadMessage);
            } catch (Exception e) {
                String reloadFailedMessage = plugin.getPluginConfig().getString("messages.reloadfailed");
                sender.sendMessage(reloadFailedMessage);
            }
        } else {
            String noPermissionMessage = plugin.getPluginConfig().getString("messages.nopermission");
            sender.sendMessage(noPermissionMessage);
        }
    }
}