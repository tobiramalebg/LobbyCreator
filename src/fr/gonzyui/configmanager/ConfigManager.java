package fr.gonzyui.configmanager;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

import fr.gonzyui.Main;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class ConfigManager {
    Configuration config;

    Configuration message;

    public Configuration getConfig() {
        if (this.config == null)
            reloadConfig();
        return this.config;
    }

    public Configuration getMessage() {
        if (this.message == null)
            reloadMessage();
        return this.message;
    }

    public void reloadMessage() {
        try {
            this.message = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(Main.getInstance().getDataFolder(), "message.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reloadConfig() {
        try {
            this.config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(Main.getInstance().getDataFolder(), "config.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void registerMessage() {
        File file = new File(Main.getInstance().getDataFolder(), "message.yml");
        if (!file.exists())
            try {
                InputStream in = Main.getInstance().getResourceAsStream("message.yml");
                Files.copy(in, file.toPath(), new java.nio.file.CopyOption[0]);
                this.message = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(Main.getInstance().getDataFolder(), "message.yml"));
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    public void registerConfig() {
        if (!Main.getInstance().getDataFolder().exists()) {
            Main.getInstance().getDataFolder().mkdir();
            File file = new File(Main.getInstance().getDataFolder(), "config.yml");
            if (!file.exists())
                try {
                    InputStream in = Main.getInstance().getResourceAsStream("config.yml");
                    Files.copy(in, file.toPath(), new java.nio.file.CopyOption[0]);
                    this.config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(Main.getInstance().getDataFolder(), "config.yml"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }
}
