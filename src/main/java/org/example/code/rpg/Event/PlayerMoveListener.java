package org.example.code.rpg.Event;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.example.code.rpg.RPG;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class PlayerMoveListener implements Listener {

    private HashMap<UUID, BossBar> playerBossBars;
    private Map<UUID, Double> playerO2;
    private Map<UUID, BukkitRunnable> activeTimers;
    private Map<UUID, Long> lastMessageTime; // 마지막 메시지 시간을 기록하는 맵
    private final double initialTime = 600.0;
    private final double maxTime = 1800.0; // 최대 시간
    private RPG plugin;

    public PlayerMoveListener(RPG plugin, HashMap<UUID, BossBar> playerBossBars, Map<UUID, Double> playerO2) {
        this.plugin = plugin;
        this.playerBossBars = playerBossBars;
        this.playerO2 = playerO2;
        this.activeTimers = new HashMap<>();
        this.lastMessageTime = new HashMap<>(); // 초기화
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();
        BossBar bossBar = playerBossBars.get(playerId);
        double remainingTime = playerO2.getOrDefault(playerId, initialTime);

        // 광석으로 늘릴 수 있는 최대 시간 제한
        if (remainingTime > maxTime) {
            long currentTime = System.currentTimeMillis();
            long lastTime = lastMessageTime.getOrDefault(playerId, 0L);

            // 10초 이내에 동일한 메시지를 보내지 않도록 설정
            if ((currentTime - lastTime) >= 10000) {
                player.sendMessage("광석으로 늘릴 수 있는 최대 시간은 30분 입니다.");
                lastMessageTime.put(playerId, currentTime); // 마지막 메시지 시간 업데이트
            }

            remainingTime = maxTime;
            playerO2.put(playerId, remainingTime);
        }

        double y = player.getLocation().getY();
        if (y < 60) {
            // 지하에 있을 때
            if (bossBar != null) {
                bossBar.setVisible(true);
                bossBar.setTitle("산소 고갈까지 남은 시간 : " + formatTime(remainingTime));
                if(remainingTime <= 0) {
                    remainingTime = 0;
                }
                bossBar.setProgress(clamp(remainingTime / initialTime, 0.0, 1.0));
            }

            if (!activeTimers.containsKey(playerId)) {
                BukkitRunnable timer = new BukkitRunnable() {
                    @Override
                    public void run() {
                        BossBar currentBossBar = playerBossBars.get(playerId);
                        if (player.isOnline() && player.getLocation().getY() < 60) {
                            Double timeLeftObj = playerO2.get(playerId);
                            if (timeLeftObj == null) {
                                timeLeftObj = initialTime;
                            }
                            double timeLeft = timeLeftObj;
                            if (timeLeft <= 0) {
                                player.damage(5);
                                if(player.getGameMode() == GameMode.CREATIVE) {
                                    player.setHealth(0);
                                }
                                if (currentBossBar != null) {
                                    currentBossBar.setVisible(false);
                                }
                                this.cancel();
                                activeTimers.remove(playerId);
                            } else {
                                timeLeft -= 1.0;

                                if (timeLeft > maxTime) {
                                    long currentTime = System.currentTimeMillis();
                                    long lastTime = lastMessageTime.getOrDefault(playerId, 0L);

                                    // 10초 이내에 동일한 메시지를 보내지 않도록 설정
                                    if ((currentTime - lastTime) >= 10000) {
                                        player.sendMessage("광석으로 늘릴 수 있는 최대 시간은 30분 입니다.");
                                        lastMessageTime.put(playerId, currentTime); // 마지막 메시지 시간 업데이트
                                    }

                                    timeLeft = maxTime;
                                }

                                playerO2.put(playerId, timeLeft);
                                if (currentBossBar != null) {
                                    currentBossBar.setProgress(clamp(timeLeft / initialTime, 0.0, 1.0));
                                    currentBossBar.setTitle("산소 고갈까지 남은 시간 : " + formatTime(timeLeft));
                                }
                                if (timeLeft % 30 == 0) {
                                    createLava(player);
                                }
                            }
                        } else {
                            if (currentBossBar != null) {
                                currentBossBar.setVisible(false);
                            }
                            this.cancel(); // 지상에 있을 때 타이머를 멈추지만 초기화하지 않음
                            activeTimers.remove(playerId);
                        }
                    }
                };
                timer.runTaskTimer(plugin, 20, 20); // 1초(20틱)마다 실행
                activeTimers.put(playerId, timer);
            }
        } else {
            // 지상에 있을 때
            if (bossBar != null) {
                bossBar.setVisible(false);
            }

            if (activeTimers.containsKey(playerId)) {
                activeTimers.get(playerId).cancel(); // 타이머를 멈추기만 하고 초기화하지 않음
                activeTimers.remove(playerId);
            }
        }
    }

    private void createLava(Player player) {
        Location playerLocation = player.getLocation();
        Random random = new Random();

        int randomX = random.nextInt(3) - 1; // -1, 0, 1
        int randomY = random.nextInt(3) - 1; // -1, 0, 1

        Location lavaLocation = playerLocation.clone().add(randomX, randomY, 0);
        Block block = lavaLocation.getBlock();
        if (block.getType() == Material.STONE) {
            block.setType(Material.LAVA);
        }
    }

    private String formatTime(double seconds) {
        int minutes = (int) (seconds / 60);
        int secs = (int) (seconds % 60);
        return String.format("%02d분 %02d초", minutes, secs);
    }

    private double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }
}