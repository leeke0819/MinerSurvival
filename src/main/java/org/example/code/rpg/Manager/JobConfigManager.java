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
        return jobBookName.contains("광부");
    }

    public String getPlayerJob(Player player) {
        FileConfiguration config = plugin.getConfig();
        String job = config.getString("users." + player.getUniqueId().toString() + ".job", "직업 없음");
        String level = config.getString("users." + player.getUniqueId().toString() + ".level", "1차");
        return job + "," + level;
    }
}
