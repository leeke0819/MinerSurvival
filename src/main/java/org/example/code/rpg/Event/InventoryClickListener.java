package org.example.code.rpg.Event;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import static org.bukkit.Bukkit.getLogger;

public class InventoryClickListener implements Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        getLogger().info("1");
        if(!event.getView().getTitle().equals("test")) return;
        event.setCancelled(true);
        getLogger().info("2");
        Player player = (Player) event.getWhoClicked();
        getLogger().info("3");
        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null || clickedItem.getType() == Material.AIR) return;

        // 아이템 클릭에 따른 행동 정의
        if (clickedItem.getType() == Material.STONE) {
            player.sendMessage("You clicked on a Stone1!");
        } else if (clickedItem.getType() == Material.STONE) {
            player.sendMessage("You clicked on an Stone2!");
        }
    }
}
