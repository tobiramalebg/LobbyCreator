package fr.gonzyui.commands;

import fr.gonzyui.Main;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class LobbyRemove extends Command {
    public LobbyRemove(String name) {
        super(name);
    }

    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer) {
            ProxiedPlayer pp = (ProxiedPlayer)sender;
            if (pp.hasPermission("lobby.remove") || pp.hasPermission("lobby.*")) {
                if (args.length <= 0) {
                    pp.sendMessage(Main.getInstance().getMessage("message.lobby.remove.arguments"));
                    return;
                }
                ServerInfo sv = Main.getInstance().getProxy().getServerInfo(args[0]);
                if (Main.getInstance().getLobbys().contains(sv.getName())) {
                    Main.getInstance().getLobbys().remove(sv.getName());
                    Main.getInstance().saveLobbysinConfig();
                    pp.sendMessage(Main.getInstance().getMessage("message.lobby.remove.message"));
                } else {
                    pp.sendMessage(Main.getInstance().getMessage("message.lobby.remove.error"));
                }
            } else {
                pp.sendMessage(Main.getInstance().getMessage("message.nopermission"));
            }
        }
    }
}
