package com.young.prisoncells;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.young.prisoncells.cells.PrisonCellListeners;
import com.young.prisoncells.cells.PrisonCellManager;
import com.young.prisoncells.cells.commands.*;
import com.young.prisoncells.cells.gui.GuiListeners;
import com.young.prisoncells.cells.gui.GuiManager;
import com.young.prisoncells.utilities.command.CommandFramework;
import lombok.Getter;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class PrisonCells extends JavaPlugin {

    private static PrisonCells instance;
    private MongoDatabase mongoDatabase;
    private Economy econonmy = null;
    private PrisonCellManager prisonCellManager;
    private GuiManager guiManager;
    private CommandFramework commandFramework;

    @Override
    public void onEnable() {
        instance = this;

        getConfig().options().copyDefaults(true);
        saveDefaultConfig();

        if (!setupEconomy() ) {
            System.out.println(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        initializeDatabase();

        prisonCellManager = new PrisonCellManager();
        guiManager = new GuiManager();
        commandFramework = new CommandFramework(this);

        registerCommands();
        registerListeners();
    }

    @Override
    public void onDisable() {
        prisonCellManager.save();
    }

    private void registerCommands() {
        commandFramework.registerCommands(new TestMessageCommand());
        commandFramework.registerCommands(new PrisonCellCommand());
        commandFramework.registerCommands(new PrisonCellCreateCommand());
        commandFramework.registerCommands(new PrisonCellRemoveCommand());
        commandFramework.registerCommands(new PrisonCellSelectCommand());
        commandFramework.registerCommands(new PrisonCellInformationCommand());
        commandFramework.registerCommands(new PrisonCellSetRegionCommand());
        commandFramework.registerCommands(new PrisonCellSetRentalPriceCommand());
        commandFramework.registerCommands(new PrisonCellAddSignCommand());
        commandFramework.registerCommands(new PrisonCellRemoveSignCommand());
        commandFramework.registerCommands(new PrisonCellAddDoorCommand());
        commandFramework.registerCommands(new PrisonCellRemoveDoorCommand());
        commandFramework.registerCommands(new PrisonCellListCommand());
        commandFramework.registerCommands(new PrisonCellSetTenantCommand());
        commandFramework.registerCommands(new PrisonCellAddMemberCommand());
        commandFramework.registerCommands(new PrisonCellRemoveMemberCommand());
        commandFramework.registerCommands(new PrisonCellLeaveCommand());
    }

    private void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new PrisonCellListeners(), this);
        Bukkit.getPluginManager().registerEvents(new GuiListeners(), this);
    }

    private void initializeDatabase() {
        MongoClient client = MongoClients.create(getConfig().getString("mongo.uri"));
        mongoDatabase = client.getDatabase(getConfig().getString("mongo.database"));
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null)
            return false;

        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);

        if (rsp == null)
            return false;

        econonmy = rsp.getProvider();

        return econonmy != null;
    }


    public WorldGuardPlugin getWorldGuard() {
        if (getConfig().getBoolean("worldguard")) {
            Plugin plugin = getServer().getPluginManager().getPlugin("WorldGuard");

            if (!(plugin instanceof WorldGuardPlugin)) {
                System.out.println("WorldGuard is enabled in the config, but could not be found.");
                return null;
            }

            return (WorldGuardPlugin) plugin;
        }

        return null;
    }

    public static PrisonCells getInstance() {
        return instance;
    }
}
