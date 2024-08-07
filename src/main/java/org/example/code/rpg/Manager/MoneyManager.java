package org.example.code.rpg.Manager;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.example.code.rpg.RPG;

public class MoneyManager {
    private RPG plugin;
    private FileConfiguration config;

    public MoneyManager(RPG plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
    }

    // 플레이어 돈 가져오기
    public int getBalance(Player player) {
        return config.getInt("users." + player.getUniqueId().toString() + ".economy", 0); // Changed to int
    }

    // 플레이어 돈 설정
    public void setBalance(Player player, int amount) {
        config.set("users." + player.getUniqueId().toString() + ".economy", amount);
        plugin.saveConfig();
    }

    // 플레이어 돈 추가
    public void addBalance(Player player, int amount) {
        setBalance(player, getBalance(player) + amount);
    }

    // 플레이어 돈 빼기
    public void subtractBalance(Player player, int amount) {
        int currentBalance = getBalance(player);
        if (currentBalance >= amount) {
            setBalance(player, currentBalance - amount);
        } else {
            // 잔액보다 더 많은 금액을 빼려고 할 때
            setBalance(player, 0); // 잔액이 음수가 되지 않도록 처리
        }
    }
}
