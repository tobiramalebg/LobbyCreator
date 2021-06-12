package fr.gonzyui.objects;

import java.util.Comparator;
import net.md_5.bungee.api.config.ServerInfo;

public class ShortUtil implements Comparator<ServerInfo> {
    public int compare(ServerInfo o1, ServerInfo o2) {
        Integer a = Integer.valueOf(o1.getPlayers().size());
        Integer b = Integer.valueOf(o2.getPlayers().size());
        return a.compareTo(b);
    }
}
