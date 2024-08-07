package org.example.code.rpg.Manager;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.example.code.rpg.Event.RenameAnvilListener;
import org.example.code.rpg.RPG;

import java.util.ArrayList;
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

    public String getPlayerJob(Player player) {
        FileConfiguration config = plugin.getConfig();
        String job = config.getString("users." + player.getUniqueId().toString() + ".job", "직업 없음");
        String level = config.getString("users." + player.getUniqueId().toString() + ".level", "1차");
        return job + "," + level;
    }

    // 직업 전직책 이름 체크 함수(switch case문 너무 길어져서 이걸로 변경)
    public boolean jobBookNameCheck(String jobBookName) {
        List<String> minerBooks = Arrays.asList("광부 1차", "광부 2차", "광부 3차", "광부 4차");
        for (String minerBook : minerBooks) {
            if (jobBookName.contains(minerBook)) {
                return true;
            }
        }
        return false;
    }

    public void createCustomItem(Player player, String command,String job) {
        ItemStack customItem = new ItemStack(Material.ENCHANTED_BOOK); // 인챈트된 책으로 커스텀아이템을 생성
        ItemMeta customItemData = customItem.getItemMeta(); // 위에서 생성된 아이템의 데이터를 커스텀아이템데이터로 불러옴.
        String jobColor = "";

        // setDisplayName으로 아이템 이름 설정
        if (command.equals("광부")) {
            jobColor = "&7&l";
        }
        customItemData.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&e&l[전직] " + "&r" + jobColor + command + " " + job));
        // 커스텀아이템데이터 설명을 저장할 리스트를 추가함으로써 기존의 아이템 설명을 덮어씀.
        // 즉, 커스텀아이템데이터의 새로운 설명을 저장하는 게 customItemExplain임.
        List<String> customItemExplain = new ArrayList<>();
        customItemExplain.add(ChatColor.RESET + "" + ChatColor.DARK_PURPLE + command + " " + job + "로 전직합니다.");
        customItemData.setLore(customItemExplain); // 커스텀아이템데이터에 커스텀아이템설명을 설정(아직 커스텀 아이템에 커스텀한 설명을 저장 안함)
        customItem.setItemMeta(customItemData); // 커스텀아이템에 커스텀아이템데이터에 저장된 값을 설정함.
        player.getInventory().addItem(customItem); // 플레이어 인벤토리에 커스텀 아이템 지급
    }

    public void giveCustomItemToPlayer(Player player, ItemStack item) {
        if (player.getInventory().firstEmpty() == -1) {
            // 인벤토리가 가득 찼을 경우
            player.getWorld().dropItem(player.getLocation(), item);
            player.sendMessage(ChatColor.RED + "인벤토리가 가득 차서 아이템이 바닥에 떨어졌습니다!");
        } else {
            // 인벤토리에 공간이 있을 경우
            player.getInventory().addItem(item);
        }
    }
}
