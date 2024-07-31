package org.example.code.rpg.Event;

import org.bukkit.Bukkit;
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
    private final double initialTime = 600.0;
    private final double maxTime = 1800.0; // Maximum time limit
    private RPG plugin;

    public PlayerMoveListener(RPG plugin, HashMap<UUID, BossBar> playerBossBars, Map<UUID, Double> playerO2) {
        this.plugin = plugin;
        this.playerBossBars = playerBossBars;
        this.playerO2 = playerO2;
        this.activeTimers = new HashMap<>();
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();
        BossBar bossBar = playerBossBars.get(playerId);
        double remainingTime = playerO2.getOrDefault(playerId, initialTime);

        // 광석으로 늘릴 수 있는 최대 시간 제한
        if (remainingTime > maxTime) {
            player.sendMessage("광석으로 늘릴 수 있는 최대 시간은 30분 입니다.");
            remainingTime = maxTime;
            playerO2.put(playerId, remainingTime);
        }

        double y = player.getLocation().getY();
        if (y < 60) {
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
                            Double timeLeftObj = playerO2.get(playerId); // 플레이어의 남은 산소 시간을 timeLeft로 가져오기
                            if (timeLeftObj == null) {
                                timeLeftObj = initialTime;
                            }
                            double timeLeft = timeLeftObj;
                            if (timeLeft <= 0) {
                                player.damage(5); // 플레이어 죽이기

                                // 보스바가 null이 아니라면
                                if (currentBossBar != null) {
                                    currentBossBar.setVisible(false); // 보스바 숨기기
                                }
                                this.cancel(); // 타이머 작업 취소
                                activeTimers.remove(playerId); // 플레이어의 타이머 제거
                            } else {
                                timeLeft -= 1.0; // 플레이어의 남은 산소 시간 1초씩 감소

                                if (timeLeft > maxTime) {
                                    player.sendMessage("광석으로 늘릴 수 있는 최대 시간은 30분 입니다.");
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
                            playerO2.put(playerId, initialTime); // 지상에 있을 때 시간 초기화
                            this.cancel();
                            activeTimers.remove(playerId);
                        }
                    }
                };
                timer.runTaskTimer(plugin, 20, 20); // 1초(20틱)마다 실행
                activeTimers.put(playerId, timer);
            }
        } else {
            if (bossBar != null) {
                bossBar.setVisible(false);
            }
            playerO2.put(playerId, initialTime); // 지상에 있을 때 시간 초기화

            if (activeTimers.containsKey(playerId)) {
                activeTimers.get(playerId).cancel();
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
        if(block.getType() == Material.STONE) {
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
