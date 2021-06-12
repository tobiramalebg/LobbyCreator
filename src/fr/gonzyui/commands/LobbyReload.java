package fr.gonzyui.commands;

import fr.gonzyui.Main;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class LobbyReload extends Command {
    public LobbyReload(String name) {
        super(name);
    }

    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer) {
            ProxiedPlayer pp = (ProxiedPlayer)sender;
            if (pp.hasPermission("lobby.reload") || pp.hasPermission("lobby.*")) {
                Main.getInstance().reloadConfig();
                pp.sendMessage(Main.getInstance().getMessage("message.lobby.reload"));
            } else {
                pp.sendMessage(Main.getInstance().getMessage("message.nopermission"));
            }
        }
    }
}
