package org.example.code.rpg;

import org.bukkit.boss.BossBar;
import org.bukkit.plugin.java.JavaPlugin;
import org.example.code.rpg.Command.*;
import org.example.code.rpg.Event.*;
import org.example.code.rpg.Manager.GuiManager;
import org.example.code.rpg.Manager.JobConfigManager;
import org.example.code.rpg.Manager.MoneyManager;
import org.example.code.rpg.Manager.NameChangeManager;
import org.example.code.rpg.Manager.PlayerScoreboardManager;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class RPG extends JavaPlugin {
    private GuiManager guiManager;
    private JobConfigManager jobConfigManager;
    private MoneyManager moneyManager;
    private PlayerScoreboardManager scoreboardManager;
    private NameChangeManager nameChangeManager;
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
        this.getCommand("이름변경권").setExecutor(new NameChangeTokenCommand(this));

        guiManager = new GuiManager(this);
        jobConfigManager = new JobConfigManager(this);
        moneyManager = new MoneyManager(this);
        scoreboardManager = new PlayerScoreboardManager(this);
        nameChangeManager = new NameChangeManager(this);

        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this, playerBossBars, playerO2), this);
        getServer().getPluginManager().registerEvents(new PlayerMoveListener(this, playerBossBars, playerO2), this);
        getServer().getPluginManager().registerEvents(new BlockBreakListener(this, playerO2), this);
        getServer().getPluginManager().registerEvents(new PlayerAttackedListener(playerO2), this);
        getServer().getPluginManager().registerEvents(new MonsterDamageListener(), this);
<<<<<<< HEAD
        getServer().getPluginManager().registerEvents(new RightClickListener(this), this);
        getServer().getPluginManager().registerEvents(new InventoryClickListener(this, guiManager, moneyManager, scoreboardManager), this);
        getServer().getPluginManager().registerEvents(new RenameAnvilListener(), this);
        getServer().getPluginManager().registerEvents(new NameChangeListener(this), this);
        getServer().getPluginManager().registerEvents(new UnableInstallBedListener(), this);
        getServer().getPluginManager().registerEvents(new WorldInitListener(), this);
=======
        getServer().getPluginManager().registerEvents(new InventoryClickListener(this, guiManager, moneyManager, scoreboardManager), this);
        getServer().getPluginManager().registerEvents(new RenameAnvilListener(), this);
        getServer().getPluginManager().registerEvents(new NameChangeListener(this), this);
>>>>>>> fff834ad3e1bf702bfaff49ad9ef7bbd141dbd72
        getServer().getPluginManager().registerEvents(nameChangeManager, this);
    }

    @Override
    public void onDisable() {
        getLogger().info("MinerSurvival Plugin 적용이 해제되었습니다.");
        this.saveDefaultConfig();
    }

    public JobConfigManager getJobConfig() {
        return jobConfigManager;
    }

    public GuiManager getGuiManager() {
        return guiManager;
    }

    public MoneyManager getMoneyManager() {
        return moneyManager;
    }

    public PlayerScoreboardManager getScoreboardManager() {
        return scoreboardManager;
    }

    public NameChangeManager getNameChangeManager() {
        return nameChangeManager;
    }
}