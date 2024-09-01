package org.example.code.rpg.Manager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;
import org.example.code.rpg.RPG;

public class NameTagChangerManager {
    private RPG plugin;
    public NameTagChangerManager(RPG plugin) {
        this.plugin = plugin;
    }
    public static void changePlayerNameTag(Player player, String newName) {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard board = manager.getMainScoreboard();

        // 플레이어가 포함될 팀을 가져오거나 생성
        Team team = board.getTeam("customNameTagTeam");
        if (team == null) {
            team = board.registerNewTeam("customNameTagTeam");
        }

        // 팀 설정 변경
        team.setPrefix(ChatColor.GREEN + "");
        team.setSuffix(ChatColor.WHITE + "");
        team.setColor(ChatColor.YELLOW); // 이름 색상 변경
        team.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.ALWAYS); // 이름 태그 항상 보이기
        team.addEntry(player.getName()); // 팀에 플레이어 추가

        // 플레이어의 이름 설정
        player.setCustomName(newName);
        player.setCustomNameVisible(true);
        player.setDisplayName(newName);
        player.setPlayerListName(newName);

        // 플레이어 스코어보드 설정
        player.setScoreboard(board);
    }
}
