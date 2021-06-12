package fr.gonzyui.commands;

import fr.gonzyui.Main;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class LobbyList extends Command {
    public LobbyList(String name) {
        super(name);
    }

    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer) {
            ProxiedPlayer pp = (ProxiedPlayer)sender;
            if (pp.hasPermission("lobby.list") || pp.hasPermission("lobby.*")) {
                pp.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.getInstance().getMessage("message.lobby.list.header")));
                for (String sv : Main.getInstance().getLobbys()) {
                    ServerInfo svi = Main.getInstance().getProxy().getServerInfo(sv);
                    int amount = svi.getPlayers().size();
                    pp.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.getInstance().getMessage("message.lobby.list.message")
                            .replaceAll("%lobby%", sv).replaceAll("%amount%", Integer.toString(amount))));
                }
                pp.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.getInstance().getMessage("message.lobby.list.footer")));
            } else {
                pp.sendMessage(Main.getInstance().getMessage("message.nopermission"));
            }
        }
    }
}
