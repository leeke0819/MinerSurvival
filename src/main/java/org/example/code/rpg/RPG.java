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
        this.getCommand("나무꾼").setExecutor(new JobCommand(this));
        this.getCommand("농부").setExecutor(new JobCommand(this));
        this.getCommand("사냥꾼").setExecutor(new JobCommand(this));
        this.getCommand("어부").setExecutor(new JobCommand(this));
        this.getCommand("연금술사").setExecutor(new JobCommand(this));
        this.getCommand("돈").setExecutor(new MoneyCommand(this));
        this.getCommand("도움말").setExecutor(new PluginHelpCommand(this));
        getServer().getPluginManager().registerEvents(new RightClickListener(this), this);
        getServer().getPluginManager().registerEvents(new UnableInstallBedListener(), this);
        this.jobConfigManager = new JobConfigManager(this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this, playerBossBars), this);
        getServer().getPluginManager().registerEvents(new BlockBreakListener(this, playerO2), this);
        getServer().getPluginManager().registerEvents(new PlayerAttackedListener(playerO2), this);
        getServer().getPluginManager().registerEvents(new MonsterDamageListener(), this);
        startPlayerCoordinateChecker();
    }

    @Override
    public void onDisable() {
        getLogger().info("MinerSurvival Plugin 적용이 해제되었습니다.");
        this.saveDefaultConfig();
    }

    public JobConfigManager getJobConfig() {
        return jobConfigManager;
    }

    private void startPlayerCoordinateChecker() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                    double y = player.getLocation().getY();
                    //getLogger().info(player.getName() + "'s Y : " + y);
                    BossBar bossBar = playerBossBars.get(player.getUniqueId());
                    if(y < 60) {
                        double time = playerO2.get(player.getUniqueId());
                        if(time > 1) {
                            player.setHealth(0);
                            return;
                        }
                        if(time < 0) {
                            time = 0;
                        }
                        bossBar.setVisible(true);
                        bossBar.setProgress(1-time); // 1 - (-1)
                        getLogger().info(String.valueOf(time));
                        time += 0.1;
                        playerO2.put(player.getUniqueId(), time);
                    } else {
                        bossBar.setVisible(false);
                        bossBar.setProgress(1);
                        playerO2.put(player.getUniqueId(), 0.0);
                    }
                }
            }
        }.runTaskTimer(this, 0L, 20L * 10); // 0L은 초기 지연 없음, 20L * 10은 10초마다 반복
    }
}