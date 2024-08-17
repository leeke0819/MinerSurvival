package org.example.code.rpg;

import org.bukkit.Bukkit;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.example.code.rpg.Command.*;
import org.example.code.rpg.Event.*;
import org.example.code.rpg.Manager.GuiManager;
import org.example.code.rpg.Manager.JobConfigManager;
import org.example.code.rpg.Manager.MoneyManager;
import org.example.code.rpg.Manager.NameChangeManager;
import org.example.code.rpg.Manager.PlayerScoreboardManager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.logging.Level;

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
        getServer().getPluginManager().registerEvents(new RightClickListener(this), this);
        getServer().getPluginManager().registerEvents(new InventoryClickListener(this, guiManager, moneyManager, scoreboardManager), this);
        getServer().getPluginManager().registerEvents(new RenameAnvilListener(), this);
        getServer().getPluginManager().registerEvents(new NameChangeListener(this), this);
        getServer().getPluginManager().registerEvents(new UnableInstallBedListener(), this);
        getServer().getPluginManager().registerEvents(new EntitySpawnListener(), this);
        getServer().getPluginManager().registerEvents(nameChangeManager, this);


        // 커스텀 구조물 데이터팩 불러오기
        boolean dataPackCopied = false;

        try {
            File dataPackFolder = new File(Bukkit.getWorldContainer(), "world/datapacks/altar");
            if (!dataPackFolder.exists()) {
                dataPackFolder.mkdirs();
                copyResource("data/altar/worldgen/structure/ancient_altar.json", new File(dataPackFolder, "worldgen/structure/ancient_altar.json"));
                copyResource("data/altar/worldgen/structure_set/ancient_altar.json", new File(dataPackFolder, "worldgen/structure_set/ancient_altar.json"));
                copyResource("data/altar/worldgen/template_pool/ancient_altar/altar_centers.json", new File(dataPackFolder, "worldgen/template_pool/ancient_altar/altar_centers.json"));
            }
        } catch (IOException e) {
            getLogger().log(Level.SEVERE, "데이터 팩 파일을 복사하지 못했습니다.", e);
        }

        if (dataPackCopied) {
            getLogger().info("데이터 팩 파일이 복사되었습니다. 변경 사항을 적용하려면 서버를 실행/reload를 하거나 다시 시작하십시오.");
        }
    }

    @Override
    public void onDisable() {
        getLogger().info("MinerSurvival Plugin 적용이 해제되었습니다.");
        this.saveDefaultConfig();
    }

    private void copyResource(String resourcePath, File dest) throws IOException {
        Files.copy(Objects.requireNonNull(getResource(resourcePath)), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
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

    // Save the clue state
    public void saveClueState(Player player, String clue, boolean unlocked) {
        getConfig().set("users." + player.getUniqueId().toString() + "." + clue, unlocked);
        saveConfig();
    }

    // Load the clue state
    public boolean loadClueState(Player player, String clue) {
        return getConfig().getBoolean("users." + player.getUniqueId().toString() + "." + clue, false);
    }
}