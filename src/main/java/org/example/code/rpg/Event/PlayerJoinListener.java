package org.example.code.rpg.Event;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.example.code.rpg.Command.NameChangeTokenCommand;
import org.example.code.rpg.Manager.MoneyManager;
import org.example.code.rpg.Manager.PlayerScoreboardManager;
import org.example.code.rpg.RPG;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.bukkit.Bukkit.createPlayerProfile;
import static org.bukkit.Bukkit.getServer;

public class PlayerJoinListener implements Listener {
    private PlayerScoreboardManager playerScoreboardManager;
    private MoneyManager moneyManager;
    private NameChangeTokenCommand nameChangeTokenCommand;
    private HashMap<UUID, BossBar> playerBossBars;
    private Map<UUID, Double> playerO2;
    private final double initialTime = 600.0;
    private RPG plugin;

    public PlayerJoinListener(RPG plugin, HashMap<UUID, BossBar> playerBossBars, Map<UUID, Double> playerO2) {
        this.plugin = plugin;
        this.playerScoreboardManager = new PlayerScoreboardManager(plugin);
        this.moneyManager = new MoneyManager(plugin);
        this.nameChangeTokenCommand = new NameChangeTokenCommand(plugin);
        this.playerBossBars = playerBossBars;
        this.playerO2 = playerO2;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        // config에서 플레이어의 사용자 지정 이름 가져오기
        FileConfiguration config = plugin.getConfig();
        String playerName = config.getString("users." + player.getUniqueId().toString() + ".name", player.getName());

        // 사용자 지정 이름을 설정했는지 확인
        if (playerName == null || playerName.isEmpty()) {
            playerName = player.getName(); // 마인크래프트 고유의 닉네임
        }

        // 이름 변경하기(플레이어 위에 뜨는 마인크래프트 고유 닉네임, Tab누르면 뜨는 플레이어 목록 리스트에 뜨는 닉네임)
        player.setDisplayName(playerName);
        player.setPlayerListName(playerName);

        event.setJoinMessage(ChatColor.GREEN + "[+] " + ChatColor.WHITE + playerName);

        // 플레이어가 이전에 접속한 적이 있는지 확인 -> hasPlayedBefore() 메서드
        if (!player.hasPlayedBefore()) {
            player.sendMessage("서버에 들어오신 걸 환영합니다!");
            moneyManager.setBalance(event.getPlayer(), 1000); // 처음 접속할 때 1000원 지급
            plugin.getJobConfig().jobCreate(player, "직업 없음", " "); // 직업, 직업 레벨 기본값 설정
            giveNameChangeTicket(player);
        }

        BossBar bossBar = getServer().createBossBar("산소 고갈까지 남은 시간 : 10분 00초", BarColor.GREEN, BarStyle.SOLID);
        bossBar.addPlayer(player);
        bossBar.setProgress(1.0); // 진행률을 100%로 설정
        bossBar.setVisible(false);
        playerBossBars.put(player.getUniqueId(), bossBar);
        playerO2.put(player.getUniqueId(), initialTime);
        playerScoreboardManager.setPlayerScoreboard(player);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        BossBar bossBar = playerBossBars.remove(event.getPlayer().getUniqueId());
        if (bossBar != null) {
            bossBar.removeAll();
        }
        // config에서 플레이어의 사용자 지정 이름 가져오기
        FileConfiguration config = plugin.getConfig();
        String playerName = config.getString("users." + player.getUniqueId().toString() + ".name", player.getName());

        // 이름 변경하기(플레이어 위에 뜨는 마인크래프트 고유 닉네임, Tab누르면 뜨는 플레이어 목록 리스트에 뜨는 닉네임)
        player.setDisplayName(playerName);
        player.setPlayerListName(playerName);
        event.setQuitMessage(ChatColor.RED + "[-] " + ChatColor.WHITE + playerName);
    }

    private void giveNameChangeTicket(Player player) {
        player.getInventory().addItem(nameChangeTokenCommand.createNameChangeToken());
    }
}
