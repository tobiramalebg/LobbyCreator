package fr.gonzyui.objects;

import java.util.ArrayList;

import fr.gonzyui.Main;
import fr.gonzyui.MainPlayer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class TPMode {
    public static void tpRandomly(ProxiedPlayer pp) {
        ServerInfo sv = Main.getRandomLobby();
        if (sv == null) {
            pp.sendMessage(Main.getInstance().getMessage("message.lobby.error"));
            return;
        }
        if (Main.getInstance().getLobbys().contains(pp.getServer().getInfo().getName())) {
            pp.sendMessage(Main.getInstance().getMessage("message.lobby.already"));
            return;
        }
        MainPlayer lbp = MainPlayer.getLobbyPlayer(pp);
        if (!Main.getInstance().isRunnable()) {
            if (Main.getInstance().isDelay() && lbp.isSendCommand() && System.currentTimeMillis() - lbp.getTimecommand().longValue() <= Main.getInstance().getTimetowait().longValue()) {
                pp.sendMessage(Main.getInstance().getMessage("message.lobby.delay.message"));
                return;
            }
            lbp.setTimecommand(Long.valueOf(System.currentTimeMillis()));
            lbp.setSendCommand(true);
            pp.connect(sv);
            pp.sendMessage(Main.getInstance().getMessage("message.lobby.tp"));
            return;
        }
        if (Main.getInstance().isDelay() && !lbp.isWaiting() && lbp.isSendCommand() && System.currentTimeMillis() - lbp.getTimecommand().longValue() <= Main.getInstance().getTimetowait().longValue()) {
            pp.sendMessage(Main.getInstance().getMessage("message.lobby.delay.message"));
            return;
        }
        lbp.setTimecommand(Long.valueOf(System.currentTimeMillis()));
        lbp.setSendCommand(true);
        if (lbp.isWaiting()) {
            pp.sendMessage(Main.getInstance().getMessage("message.lobby.cancel"));
            lbp.setWaiting(false);
            lbp.getTask().cancel();
            return;
        }
        lbp.setWaiting(true);
        pp.sendMessage(Main.getInstance().getMessage("message.lobby.waiting").replaceAll("%time%", Integer.toString((Main.getInstance()).time)));
        lbp.transportWithRunnable(sv);
    }

    public static void tpLessPlayers(ProxiedPlayer pp) {
        if (Main.getInstance().getLobbys().size() <= 0) {
            pp.sendMessage(Main.getInstance().getMessage("message.lobby.error"));
            return;
        }
        if (Main.getInstance().getLobbys().contains(pp.getServer().getInfo().getName())) {
            pp.sendMessage(Main.getInstance().getMessage("message.lobby.already"));
            return;
        }
        ArrayList<ServerInfo> lobbylist = new ArrayList<>();
        for (String str : Main.getInstance().getLobbys())
            lobbylist.add(Main.getInstance().getProxy().getServerInfo(str));
        lobbylist.sort(new ShortUtil());
        ServerInfo sv = lobbylist.get(0);
        if (sv == null) {
            pp.sendMessage(Main.getInstance().getMessage("message.lobby.error"));
            return;
        }
        MainPlayer lbp = MainPlayer.getLobbyPlayer(pp);
        if (!Main.getInstance().isRunnable()) {
            if (Main.getInstance().isDelay() && lbp.isSendCommand() && System.currentTimeMillis() - lbp.getTimecommand().longValue() <= Main.getInstance().getTimetowait().longValue()) {
                pp.sendMessage(Main.getInstance().getMessage("message.lobby.delay.message"));
                return;
            }
            lbp.setTimecommand(Long.valueOf(System.currentTimeMillis()));
            lbp.setSendCommand(true);
            pp.connect(sv);
            pp.sendMessage(Main.getInstance().getMessage("message.lobby.tp"));
            return;
        }
        if (Main.getInstance().isDelay() && !lbp.isWaiting() && lbp.isSendCommand() && System.currentTimeMillis() - lbp.getTimecommand().longValue() <= Main.getInstance().getTimetowait().longValue()) {
            pp.sendMessage(Main.getInstance().getMessage("message.lobby.delay.message"));
            return;
        }
        lbp.setTimecommand(Long.valueOf(System.currentTimeMillis()));
        lbp.setSendCommand(true);
        if (lbp.isWaiting()) {
            pp.sendMessage(Main.getInstance().getMessage("message.lobby.cancel"));
            lbp.setWaiting(false);
            lbp.getTask().cancel();
            return;
        }
        lbp.setWaiting(true);
        pp.sendMessage(Main.getInstance().getMessage("message.lobby.waiting").replaceAll("%time%", Integer.toString((Main.getInstance()).time)));
        lbp.transportWithRunnable(sv);
    }
}
