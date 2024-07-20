package org.example.code.rpg.Event;

import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.example.code.rpg.Manager.MoneyManager;
import org.example.code.rpg.Manager.PlayerScoreboardManager;
import org.example.code.rpg.RPG;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.bukkit.Bukkit.getServer;

public class PlayerJoinListener implements Listener {
    private PlayerScoreboardManager playerScoreboardManager;
    private MoneyManager moneyManager;

    private HashMap<UUID, BossBar> playerBossBars;
    private Map<UUID, Double> playerO2;
    private final double initialTime = 600.0;

    public PlayerJoinListener(RPG plugin, HashMap<UUID, BossBar> playerBossBars, Map<UUID, Double> playerO2) {
        this.playerScoreboardManager = new PlayerScoreboardManager(plugin);
        this.moneyManager = new MoneyManager(plugin);
        this.playerBossBars = playerBossBars;
        this.playerO2 = playerO2;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        playerScoreboardManager.setPlayerScoreboard(event.getPlayer());
        Player player = event.getPlayer();
        player.sendMessage("서버에 들어오신 걸 환영합니다!");
        // 플레이어가 이전에 접속한 적이 있는지 확인 -> hasPlayedBefore() 메서드
        if (!player.hasPlayedBefore()) {
            moneyManager.setBalance(event.getPlayer(), 1000);  // 처음 접속할 때 1000원 지급
        }
        BossBar bossBar = getServer().createBossBar("산소 고갈까지 남은 시간 : 10분 00초", BarColor.GREEN, BarStyle.SOLID);
        bossBar.addPlayer(player);
        bossBar.setProgress(1.0); // 진행률을 100%로 설정
        bossBar.setVisible(false);
        playerBossBars.put(player.getUniqueId(), bossBar);
        playerO2.put(player.getUniqueId(), initialTime);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        BossBar bossBar = playerBossBars.remove(event.getPlayer().getUniqueId());
        if (bossBar != null) {
            bossBar.removeAll();
        }
    }
}