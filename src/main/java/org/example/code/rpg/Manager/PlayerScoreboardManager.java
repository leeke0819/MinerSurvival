package org.example.code.rpg.Manager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;
import org.example.code.rpg.RPG;

public class PlayerScoreboardManager {
    private RPG plugin;
    private Scoreboard scoreboard;
    private Objective objective;

    public PlayerScoreboardManager(RPG plugin) {
        this.plugin = plugin;
    }

    public void setPlayerScoreboard(Player player) {
        FileConfiguration config = plugin.getConfig();
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        scoreboard = manager.getNewScoreboard();
        objective = scoreboard.registerNewObjective("1", "dummy", ChatColor.BOLD + "플레이어 정보");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        Score nameScore = objective.getScore(ChatColor.translateAlternateColorCodes('&',"&l이름 : " + ChatColor.YELLOW + player.getName()));
        nameScore.setScore(3);

        String job = config.getString("users." + player.getUniqueId().toString() + ".job", "직업 없음");
        String level = config.getString("users." + player.getUniqueId().toString() + ".level", "");
        Score jobScore = objective.getScore(ChatColor.BOLD + "직업 : " + job + " " + level);
        jobScore.setScore(2);

        double balance = config.getDouble("users." + player.getUniqueId().toString() + ".economy", 0.0);
        int roundedBalance = (int) balance;
        Score moneyScore = objective.getScore(ChatColor.translateAlternateColorCodes('&', "&l돈 : " + ChatColor.YELLOW + roundedBalance + "&l원"));
        moneyScore.setScore(1);

        player.setScoreboard(scoreboard);
    }
}