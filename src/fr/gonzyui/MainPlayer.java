package fr.gonzyui;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.scheduler.ScheduledTask;

public class MainPlayer {
    private ProxiedPlayer pp;

    private boolean waiting = false;

    private ScheduledTask task;

    public static HashMap<ProxiedPlayer, MainPlayer> lobbyPlayer = new HashMap<>();

    private Integer time = Integer.valueOf(0);

    private boolean sendCommand = false;

    private Long timecommand;

    public MainPlayer(ProxiedPlayer pp) {
        this.pp = pp;
        lobbyPlayer.put(pp, this);
    }

    public static MainPlayer getLobbyPlayer(ProxiedPlayer pp) {
        if (lobbyPlayer.containsKey(pp))
            return lobbyPlayer.get(pp);
        return new MainPlayer(pp);
    }

    public ScheduledTask getTask() {
        return this.task;
    }

    public void transportWithRunnable(final ServerInfo sv) {
        this.time = Integer.valueOf(0);
        this.task = Main.getInstance().getProxy().getScheduler().schedule(Main.getInstance(), new Runnable() {
            public void run() {
                if (MainPlayer.this.time.intValue() > (Main.getInstance()).time) {
                    MainPlayer.this.pp.connect(sv);
                    MainPlayer.this.waiting = false;
                    MainPlayer.this.pp.sendMessage(Main.getInstance().getMessage("message.lobby.tp"));
                    MainPlayer.this.time = Integer.valueOf(0);
                    MainPlayer.this.task.cancel();
                    MainPlayer.this.sendCommand = false;
                }
                Integer integer1 = MainPlayer.this.time, integer2 = MainPlayer.this.time = Integer.valueOf(MainPlayer.this.time.intValue() + 1);
            }
        },  0L, 1L, TimeUnit.SECONDS);
    }

    public boolean isWaiting() {
        return this.waiting;
    }

    public void setWaiting(boolean waiting) {
        this.waiting = waiting;
    }

    public boolean isSendCommand() {
        return this.sendCommand;
    }

    public void setSendCommand(boolean sendCommand) {
        this.sendCommand = sendCommand;
    }

    public Long getTimecommand() {
        return this.timecommand;
    }

    public void setTimecommand(Long timecommand) {
        this.timecommand = timecommand;
    }
}
