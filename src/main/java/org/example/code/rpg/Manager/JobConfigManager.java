package org.example.code.rpg.Manager;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.example.code.rpg.RPG;

import java.util.Arrays;
import java.util.List;

public class JobConfigManager {
    private RPG plugin;
    public JobConfigManager(RPG plugin) {
        this.plugin = plugin;
    }
    public void jobCreate(Player player, String job, String level) {
        plugin.getConfig().set("users." + player.getUniqueId().toString() + ".job", job);
        plugin.getConfig().set("users." + player.getUniqueId().toString() + ".level", level);
        plugin.saveConfig();
    }

    // 직업 전직책 이름 체크 함수(switch case문 너무 길어져서 이걸로 변경)
    public boolean jobBookNameCheck(String jobBookName) {
        // 유효한 직업 리스트 저장할 변수 생성(새로운 요소 추가와 기존 요소 삭제를 못함.)
        List<String> validJob = Arrays.asList("광부", "나무꾼", "농부", "사냥꾼", "어부", "연금술사");
        // 여기 아래는.. 리스트를 스트림으로 변환해서 하는거라는데 원래 for문으로 하려다가 이게 조금 더 빠르다고 해서 사용해보려고요!
        return validJob.stream().anyMatch(job -> jobBookName.contains(job));
    }

    public String getPlayerJob(Player player) {
        FileConfiguration config = plugin.getConfig();
        String job = config.getString("users." + player.getUniqueId().toString() + ".job", "직업 없음");
        String level = config.getString("users." + player.getUniqueId().toString() + ".level", "1차");
        return job + "," + level;
    }
}
