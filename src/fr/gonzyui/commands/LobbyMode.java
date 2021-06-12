package fr.gonzyui.commands;

import java.io.File;
import java.io.IOException;

import fr.gonzyui.Main;
import fr.gonzyui.configmanager.ConfigManager;
import fr.gonzyui.objects.Type;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class LobbyMode extends Command {
    private ConfigurationProvider cp = ConfigurationProvider.getProvider(YamlConfiguration.class);

    private ConfigManager config = new ConfigManager();

    public LobbyMode(String name) {
        super(name);
    }

    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer) {
            ProxiedPlayer pp = (ProxiedPlayer)sender;
            if (pp.hasPermission("lobby.mode") || pp.hasPermission("lobby.*")) {
                if (args.length <= 0) {
                    pp.sendMessage(Main.getInstance().getMessage("message.lobby.mode.arguments"));
                    return;
                }
                if (args[0].equalsIgnoreCase("random") || args[0].equalsIgnoreCase("lessplayers")) {
                    if (args[0].equalsIgnoreCase("random")) {
                        Main.getInstance().setMode(Type.RANDOM);
                    } else {
                        Main.getInstance().setMode(Type.LESSPLAYERS);
                    }
                    File file = new File(Main.getInstance().getDataFolder(), "config.yml");
                    try {
                        Configuration cgf = this.cp.load(file);
                        cgf.set("config.tp_mode", args[0].toLowerCase());
                        ConfigurationProvider.getProvider(YamlConfiguration.class).save(cgf, new File(Main.getInstance().getDataFolder(), "config.yml"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    pp.sendMessage(Main.getInstance().getMessage("message.lobby.mode.sucess").replaceAll("%mode%", args[0]));
                    return;
                }
                pp.sendMessage(ChatColor.GRAY + "You only can use this options: " + ChatColor.GREEN + "random , lessplayers");
            } else {
                pp.sendMessage(Main.getInstance().getMessage("message.nopermission"));
            }
        }
    }
}
