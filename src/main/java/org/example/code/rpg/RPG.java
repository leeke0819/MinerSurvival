package org.example.code.rpg;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.boss.BossBar;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.example.code.rpg.Command.*;
import org.example.code.rpg.Event.*;
import org.example.code.rpg.Manager.*;

import org.bukkit.command.ConsoleCommandSender;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.*;
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
        locateAndSaveStructureLocation();
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
        getServer().getPluginManager().registerEvents(nameChangeManager, this);


        // 커스텀 구조물 데이터팩 불러오기
        boolean dataPackCopied = false;

        try {
            File dataPackFolder = new File(Bukkit.getWorldContainer(), "world/datapacks/altar");
            if (!dataPackFolder.exists()) {
                dataPackFolder.mkdirs();
                copyResource("data/altar/worldgen/structure/ancient_altar.j son", new File(dataPackFolder, "worldgen/structure/ancient_altar.json"));
                copyResource("data/altar/worldgen/structure_set/ancient_altar.json", new File(dataPackFolder, "worldgen/structure_set/ancient_altar.json"));
                copyResource("data/altar/worldgen/template_pool/ancient_altar/altar_centers.json", new File(dataPackFolder, "worldgen/template_pool/ancient_altar/altar_centers.json"));
                dataPackCopied = true;
            }
        } catch (IOException e) {
            getLogger().log(Level.SEVERE, "데이터 팩 파일을 복사하지 못했습니다.", e);
        }

        if (dataPackCopied) {
            getLogger().info("데이터 팩 파일이 복사되었습니다. 변경 사항을 적용하려면 서버를 실행/reload를 하거나 다시 시작하십시오.");
        }
    }
    private void locateAndSaveStructureLocation() {
        Bukkit.getScheduler().runTaskLater(this, () -> {
            //1. while문 혹은 for문 돌린다.
            World world = Bukkit.getWorld("world");
                if (world == null) {
                    getLogger().warning("월드를 찾을 수 없습니다.");
                    return;
                }

                for (int i = 0; i < 53; i++) {
                    // 2. 랜덤한 좌표 하나 받는다
                    int x = getRandomCoordinate(-3000, 3000);
                    int z = getRandomCoordinate(-3000, 3000);
                    int y = getRandomYCoordinate(world, x, z);

                    // 3. 바이옴 조회한다
                    Biome biome = world.getBiome(x, y, z);

                    // 4. if문으로 바이옴 검사한다 -> 니가 생각한 바이옴이면
                    if (biome == Biome.JUNGLE || biome == Biome.OLD_GROWTH_PINE_TAIGA || biome == Biome.OLD_GROWTH_SPRUCE_TAIGA || biome == Biome.SWAMP ||
                            biome == Biome.WOODED_BADLANDS || biome == Biome.DARK_FOREST || biome == Biome.MANGROVE_SWAMP || biome == Biome.ERODED_BADLANDS) {

                        // 5. 해당 xyz에 구조물 생성하고 config에 xyz 기록
                        ConsoleCommandSender consoleSender = Bukkit.getServer().getConsoleSender();
                        Bukkit.dispatchCommand(consoleSender, "place structure altar:ancient_altar " + x + " " + y + " " + z);

                        saveCoordinatesToConfig(x, y, z);  // 좌표를 config에 저장
                        getLogger().info("Structure placed at: (" + x + ", " + y + ", " + z + ")");
                        break;  // 루프 종료

                    } else {
                        getLogger().info("The biome at these coordinates (" + x + ", " + y + ", " + z + ") is " + biome.name() + ", not suitable.");
                        // 7. 다시 랜덤한 좌표 하나 받는다 -> 이 경우 for 루프가 다시 실행되어 반복
                    }
                }

            //8. 위 과정 반복

        }, 20L);
    }
    // 랜덤한 x, z 좌표 생성
    private int getRandomCoordinate(int min, int max) {
        Random random = new Random();
        return random.nextInt((max - min) + 1) + min;
    }

    // 랜덤한 y 좌표 생성
    private int getRandomYCoordinate(World world, int x, int z) {
        return world.getHighestBlockYAt(x, z);
    }

    // 구조물 좌표를 config.yml에 저장하는 메서드
    private void saveCoordinatesToConfig(int x, int y, int z) {
        FileConfiguration config = this.getConfig();
        config.set("structures.altar_ancient_altar.x", x);
        config.set("structures.altar_ancient_altar.y", y);
        config.set("structures.altar_ancient_altar.z", z);
        saveConfig();
        getLogger().info("Structure coordinates saved to config.yml");
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