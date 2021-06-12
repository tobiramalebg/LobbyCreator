package fr.gonzyui.commands;

import fr.gonzyui.Main;
import fr.gonzyui.objects.TPMode;
import fr.gonzyui.objects.Type;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class Lobby extends Command {
    public Lobby(String name, String permission, String[] aliases) {
        super(name, permission, aliases);
    }

    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer) {
            ProxiedPlayer pp = (ProxiedPlayer)sender;
            if (!pp.hasPermission(getPermission())) {
                pp.sendMessage(Main.getInstance().getMessage("message.nopermission"));
                return;
            }
            if (Main.getInstance().getRlobbys().contains(pp.getServer().getInfo().getName())) {
                pp.sendMessage(Main.getInstance().getMessage("message.lobby.denied"));
                return;
            }
            if (Main.getInstance().getMode() == Type.RANDOM) {
                TPMode.tpRandomly(pp);
            } else {
                TPMode.tpLessPlayers(pp);
            }
        }
    }
}
