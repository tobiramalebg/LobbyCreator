package fr.gonzyui.events;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import fr.gonzyui.Main;
import fr.gonzyui.configmanager.ConfigManager;
import fr.gonzyui.objects.TPMode;
import fr.gonzyui.objects.Type;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class Event implements Listener {
    private ConfigManager config = new ConfigManager();

    @EventHandler
    public void pluginchanel(PluginMessageEvent e) {
        String[] bytedata = readData(e.getData());
        byte b;
        int i;
        String[] arrayOfString1;
        for (i = (arrayOfString1 = bytedata).length, b = 0; b < i; ) {
            String line = arrayOfString1[b];
            if (line.equalsIgnoreCase("LBTP")) {
                ProxiedPlayer pp = Main.getInstance().getProxy().getPlayer(e.getReceiver().toString());
                if (pp != null) {
                    if (!pp.hasPermission(this.config.getConfig().getString("config.commands.lobby.permission"))) {
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
            b = (byte)(b + 1);
        }
    }

    private String[] readData(byte[] data) {
        List<String> readed = new ArrayList<>();
        DataInputStream di = new DataInputStream(new ByteArrayInputStream(data));
        for (int i = 0; i < 255; i++) {
            try {
                String dr = di.readUTF();
                readed.add(dr);
            } catch (IOException e) {
                if (readed.size() != 0) {
                    String[] arrayOfString1 = readed.<String>toArray(new String[readed.size()]);
                    return arrayOfString1;
                }
                String[] arrayOfString = { new String(data, Charset.forName("UTF-8")) };
                return arrayOfString;
            }
        }
        String[] out = readed.<String>toArray(new String[readed.size()]);
        return out;
    }
}
