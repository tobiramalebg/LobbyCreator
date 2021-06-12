package fr.gonzyui.commands;

import fr.gonzyui.Main;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class LobbyCreate extends Command {
    public LobbyCreate(String name) {
        super(name);
    }

    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer) {
            ProxiedPlayer pp = (ProxiedPlayer)sender;
            if (pp.hasPermission("lobby.create") || pp.hasPermission("lobby.*")) {
                ServerInfo sv = pp.getServer().getInfo();
                if (Main.getInstance().getLobbys().contains(sv.getName())) {
                    pp.sendMessage(Main.getInstance().getMessage("message.lobby.islobby"));
                    return;
                }
                Main.getInstance().getLobbys().add(sv.getName());
                Main.getInstance().saveLobbysinConfig();
                pp.sendMessage(Main.getInstance().getMessage("message.lobby.create"));
            } else {
                pp.sendMessage(Main.getInstance().getMessage("message.nopermission"));
            }
        }
    }
}
