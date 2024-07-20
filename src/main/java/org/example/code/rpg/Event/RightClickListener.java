package org.example.code.rpg.Event;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.example.code.rpg.RPG;

import static org.bukkit.Bukkit.getLogger;

public class RightClickListener implements Listener {
    private RPG plugin;
    public RightClickListener(RPG plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerRightClick(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Player player = event.getPlayer();
            ItemStack HandItem = player.getInventory().getItemInMainHand();
            if(HandItem.getType() == Material.ENCHANTED_BOOK && HandItem.hasItemMeta()) {
                rightClickJobChange(HandItem, player);
            }
        }
    }

    private void rightClickJobChange(ItemStack item, Player player) {
        ItemMeta meta = item.getItemMeta();
        if(meta.hasDisplayName()) {
            String jobBook = meta.getDisplayName(); // 우클릭한 책의 이름 가져오기(ex. [전직] 광부 1차 책이 있으면 '[전직] 광부 1차'가 jobBook에 저장.)
            getLogger().info(jobBook);
            if(plugin.getJobConfig().jobBookNameCheck(jobBook)) {
                String[] share = jobBook.split(" "); // 공백을 기준으로 배열 나누기
                // 배열의 길이 확인하기 (split으로 나눈 건 배열로 인정 안됨. 배열 길이 4로 오해하지 말자!)
                if(share.length == 3) {
                    plugin.getJobConfig().jobCreate(player,share[1],share[2]);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', share[1] + " " + share[2] + "&r입니다!"));
                }
            }
        }
    }

    private boolean verifyItem(ItemStack item, Player player) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.hasDisplayName()) return false;

        NamespacedKey jobKey = new NamespacedKey(plugin, "job");
        NamespacedKey levelKey = new NamespacedKey(plugin, "level");
        String job = meta.getPersistentDataContainer().get(jobKey, PersistentDataType.STRING);
        String level = meta.getPersistentDataContainer().get(levelKey, PersistentDataType.STRING);

        return job != null && level != null && plugin.getJobConfig().jobBookNameCheck(job);
    }
}