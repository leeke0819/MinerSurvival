package org.example.code.rpg;

import org.bukkit.Bukkit;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.example.code.rpg.Command.*;
import org.example.code.rpg.Event.*;
import org.example.code.rpg.Manager.JobConfigManager;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public final class RPG extends JavaPlugin {
    private JobConfigManager jobConfigManager;
    private HashMap<UUID, BossBar> playerBossBars = new HashMap<>();
    private Map<UUID, Double> playerO2 = new HashMap<>();
    @Override
    public void onEnable() {
        getLogger().info("MinerSurvival Plugin이 적용되었습니다.");
        this.saveDefaultConfig();
        this.getCommand("광부").setExecutor(new JobCommand(this));
        this.getCommand("돈").setExecutor(new MoneyCommand(this));
        this.getCommand("도움말").setExecutor(new PluginHelpCommand(this));
        this.getCommand("메뉴").setExecutor(new GuiCommand(this));
        getServer().getPluginManager().registerEvents(new RightClickListener(this), this);
        getServer().getPluginManager().registerEvents(new UnableInstallBedListener(), this);
        this.jobConfigManager = new JobConfigManager(this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this, playerBossBars, playerO2), this);
        getServer().getPluginManager().registerEvents(new PlayerMoveListener(this, playerBossBars, playerO2), this);
        getServer().getPluginManager().registerEvents(new BlockBreakListener(this, playerO2), this);
        getServer().getPluginManager().registerEvents(new PlayerAttackedListener(playerO2), this);
        getServer().getPluginManager().registerEvents(new MonsterDamageListener(), this);
        getServer().getPluginManager().registerEvents(new InventoryClickListener(this), this);
    }

    @Override
    public void onDisable() {
        getLogger().info("MinerSurvival Plugin 적용이 해제되었습니다.");
        this.saveDefaultConfig();
    }

    public JobConfigManager getJobConfig() {
        return jobConfigManager;
    }

}