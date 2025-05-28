package de.mecrytv.tBClanTag;

import de.mecrytv.tBClanTag.commands.ClanTagCommand;
import de.mecrytv.tBClanTag.database.ClanTags.ClanTagManager;
import de.mecrytv.tBClanTag.database.DatabaseManager;
import de.mecrytv.tBClanTag.utils.LogWithColor;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class TBClanTag extends JavaPlugin {

    private static TBClanTag instance;
    private YamlConfiguration config;
    private File configFile;
    private String prefix;

    private DatabaseManager databaseManager;
    private ClanTagManager clanTagManager;

    @Override
    public void onEnable() {
        instance = this;

        startLog();
        loadConfig();
        loadCommands();

        databaseManager = new DatabaseManager();
        clanTagManager = new ClanTagManager();
    }

    @Override
    public void onDisable() {
        if (databaseManager != null) {
            databaseManager.shutDown();
        }
        System.out.println(LogWithColor.color("[ClanTags] Plugin has been disabled!", LogWithColor.RED));
    }

    private void startLog() {
        String[] tbct = {
                "TTTTTBBBB    CCCC  TTTTT",
                "  T  B   B  C        T        TBCT v1.0.0",
                "  T  BBBB   C        T        Running on Bukkit - Paper",
                "  T  B   B  C        T  ",
                "  T  BBBB    CCCC    T  "
        };
        for (String line: tbct) {
            getLogger().info(LogWithColor.color(line, LogWithColor.MAGENTA));
        }

        getLogger().info(LogWithColor.color("Developed by MecryTv", LogWithColor.GOLD));
        getLogger().info(LogWithColor.color("Plugin has been enabled!", LogWithColor.GREEN));

    }
    private void loadConfig() {
        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }

        configFile = new File(getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        config = YamlConfiguration.loadConfiguration(configFile);
        config.options().copyDefaults(true);
        config.addDefault("mysql.host", "91.218.67.51");
        config.addDefault("mysql.port", 3306);
        config.addDefault("mysql.user", "minecraft");
        config.addDefault("mysql.password", "MyMcPW05");
        config.addDefault("mysql.database", "ClanTags");
        config.addDefault("prefix", "&8[&bClan Tag&8] ");

        try {
            config.save(configFile);
        } catch (Exception e) {
            throw  new RuntimeException(e);
        }

        prefix = config.getString("prefix").replace("&", "§");
    }
    private void loadCommands() {
        getCommand("tag").setExecutor(new ClanTagCommand());
    }

    public static TBClanTag getInstance() {
        return instance;
    }

    public String getPrefix() {
        return prefix;
    }

    @Override
    public YamlConfiguration getConfig() {
        return config;
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public ClanTagManager getClanTagManager() {
        return clanTagManager;
    }
}