package fr.gonzyui.commands;

import fr.gonzyui.Main;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class LobbyTP extends Command {
    public LobbyTP(String name) {
        super(name);
    }

    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer) {
            ProxiedPlayer pp = (ProxiedPlayer)sender;
            if (pp.hasPermission("lobby.tp") || pp.hasPermission("lobby.*")) {
                if (args.length <= 0) {
                    pp.sendMessage(Main.getInstance().getMessage("message.lobby.teleport.arguments"));
                    return;
                }
                ProxiedPlayer target = Main.getInstance().getProxy().getPlayer(args[0]);
                if (target == null) {
                    pp.sendMessage(Main.getInstance().getMessage("message.lobby.teleport.error"));
                    return;
                }
                ServerInfo nsv = Main.getRandomLobby();
                if (Main.getInstance().getLobbys().contains(pp.getServer().getInfo().getName())) {
                    target.connect(pp.getServer().getInfo());
                } else {
                    target.connect(nsv);
                }
                pp.sendMessage(Main.getInstance().getMessage("message.lobby.teleport.sucess")
                        .replaceAll("%player%", target.getName())
                        .replaceAll("%server%", nsv.getName()));
                target.sendMessage(Main.getInstance().getMessage("message.lobby.teleport.player"));
                return;
            }
            pp.sendMessage(Main.getInstance().getMessage("message.nopermission"));
        }
    }
}
