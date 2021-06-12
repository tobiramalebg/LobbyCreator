package fr.gonzyui;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import fr.gonzyui.commands.*;
import fr.gonzyui.configmanager.ConfigManager;
import fr.gonzyui.events.Event;
import fr.gonzyui.objects.Type;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public final class Main extends Plugin {
    private ConfigurationProvider cp = ConfigurationProvider.getProvider(YamlConfiguration.class);

    private ConfigManager config;

    private static Main instance;

    public int time;

    private boolean runnable;

    private boolean delay;

    private Long timetowait;

    private int seconds;

    private static List<String> Globbys = new ArrayList<>();

    private static List<String> Rlobbys = new ArrayList<>();

    private Type mode;

    public void onEnable() {
        instance = this;
        getProxy().registerChannel("BungeeCord");
        loadConfig();
        loadDirectory();
        loadDeniedServers();
        loadLobbysfromConfig();
        this.runnable = Boolean.parseBoolean(this.config.getConfig().getString("config.countdown"));
        this.time = Integer.parseInt(this.config.getConfig().getString("config.time"));
        this.delay = Boolean.parseBoolean(this.config.getConfig().getString("config.command_delay"));
        this.seconds = Integer.parseInt(this.config.getConfig().getString("config.delay_time"));
        this.timetowait = Long.valueOf(this.seconds + "000");
        loadCommands();
        String modo = this.config.getConfig().getString("config.tp_mode");
        switch (modo.toUpperCase()) {
            case "RANDOM":
                setMode(Type.RANDOM);
                break;
            case "LESSPLAYERS":
                setMode(Type.LESSPLAYERS);
                break;
            default:
                getProxy().getConsole().sendMessage(ChatColor.RED + "An error has ocurred trying to set the mode in config.yml -> tp_mode. Check it!");
                getProxy().stop();
                break;
        }
        getProxy().getPluginManager().registerListener(this, (Listener)new Event());
        getProxy().getConsole().sendMessage(ChatColor.GREEN + "[Lobby System] loaded correctly.");
        getProxy().getConsole().sendMessage(ChatColor.GREEN + "[Lobby System] " + getLobbys().size() + " lobbys has been added.");
    }

    public void loadConfig() {
        this.config = new ConfigManager();
        this.config.registerConfig();
        this.config.registerMessage();
    }

    public void loadDeniedServers() {
        Rlobbys.clear();
        List<String> lobbys = this.config.getConfig().getStringList("config.commands.lobby.denied_servers");
        for (String servers : lobbys) {
            if (getInstance().getProxy().getServers().containsKey(servers))
                Rlobbys.add(servers);
        }
    }

    public void loadLobbysfromConfig() {
        Globbys.clear();
        List<String> lobbys = this.config.getConfig().getStringList("config.lobbys");
        for (String servers : lobbys) {
            if (getInstance().getProxy().getServers().containsKey(servers))
                Globbys.add(servers);
        }
    }

    public void saveLobbysinConfig() {
        File file = new File(getDataFolder(), "config.yml");
        try {
            Configuration cgf = this.cp.load(file);
            cgf.set("config.lobbys", Globbys);
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(cgf, new File(getDataFolder(), "config.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveMessage() {
        File file = new File(getDataFolder(), "message.yml");
        try {
            Configuration cgf = this.cp.load(file);
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(cgf, new File(getDataFolder(), "message.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveConfig() {
        File file = new File(getDataFolder(), "config.yml");
        try {
            Configuration cgf = this.cp.load(file);
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(cgf, new File(getDataFolder(), "config.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reloadConfig() {
        saveConfig();
        saveMessage();
        this.config.reloadConfig();
        this.config.reloadMessage();
        loadLobbysfromConfig();
        loadDeniedServers();
        this.runnable = Boolean.parseBoolean(this.config.getConfig().getString("config.countdown"));
        this.time = Integer.parseInt(this.config.getConfig().getString("config.time"));
        this.delay = Boolean.parseBoolean(this.config.getConfig().getString("config.command_delay"));
        this.seconds = Integer.parseInt(this.config.getConfig().getString("config.delay_time"));
        this.timetowait = Long.valueOf(this.seconds + "000");
        String modo = this.config.getConfig().getString("config.tp_mode");
        switch (modo.toUpperCase()) {
            case "RANDOM":
                setMode(Type.RANDOM);
                return;
            case "LESSPLAYERS":
                setMode(Type.LESSPLAYERS);
                return;
        }
        getProxy().getConsole().sendMessage(ChatColor.RED + "An error has ocurred trying to set the mode in config.yml -> tp_mode. Check it!");
        getProxy().stop();
    }

    public static ServerInfo getRandomLobby() {
        Random random = new Random();
        if (Globbys.size() <= 0)
            return null;
        int ran = random.nextInt(Globbys.size());
        return getInstance().getProxy().getServerInfo(Globbys.get(ran));
    }

    public List<String> getLobbys() {
        return Globbys;
    }

    public List<String> getRlobbys() {
        return Rlobbys;
    }

    private void loadCommands() {
        try {
            String name = this.config.getConfig().getString("config.commands.lobby.command");
            String permission = this.config.getConfig().getString("config.commands.lobby.permission");
            String[] aliases = (String[])this.config.getConfig().getStringList("config.commands.lobby.aliases").toArray((Object[])new String[0]);
            ProxyServer.getInstance().getPluginManager().registerCommand(this, (Command)new Lobby(name, permission, aliases));
            ProxyServer.getInstance().getPluginManager().registerCommand(this, (Command)new LobbyCreate("lobbycreate"));
            ProxyServer.getInstance().getPluginManager().registerCommand(this, (Command)new LobbyList("lobbylist"));
            ProxyServer.getInstance().getPluginManager().registerCommand(this, (Command)new LobbyTransport("lobbytransport"));
            ProxyServer.getInstance().getPluginManager().registerCommand(this, (Command)new LobbyReload("lobbyreload"));
            ProxyServer.getInstance().getPluginManager().registerCommand(this, (Command)new LobbyRemove("lobbyremove"));
            ProxyServer.getInstance().getPluginManager().registerCommand(this, (Command)new LobbyServer("lobbyserver"));
            ProxyServer.getInstance().getPluginManager().registerCommand(this, (Command)new LobbyTP("lobbytp"));
            ProxyServer.getInstance().getPluginManager().registerCommand(this, (Command)new LobbyMode("lobbymode"));
        } catch (Exception e) {
            e.printStackTrace();
            getProxy().getConsole().sendMessage(ChatColor.RED + "An error has ocurred loading commands from config.yml, fixed it");
            getProxy().stop();
        }
    }

    public String getMessage(String path) {
        return ChatColor.translateAlternateColorCodes('&', this.config.getMessage().getString("message.prefix") + this.config.getMessage().getString(path));
    }

    private void loadDirectory() {
        File dir = new File(getDataFolder().toString());
        if (!dir.exists())
            dir.mkdir();
    }

    public static Main getInstance() {
        return instance;
    }

    public boolean isRunnable() {
        return this.runnable;
    }

    public Type getMode() {
        return this.mode;
    }

    public void setMode(Type mode) {
        this.mode = mode;
    }

    public Long getTimetowait() {
        return this.timetowait;
    }

    public boolean isDelay() {
        return this.delay;
    }

    public int getseconds() {
        return this.seconds;
    }
}
