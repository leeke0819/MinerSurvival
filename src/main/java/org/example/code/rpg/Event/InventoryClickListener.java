package org.example.code.rpg.Event;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import static org.bukkit.Bukkit.getLogger;

public class InventoryClickListener implements Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if(!event.getView().getTitle().equals("메뉴")) return;
        event.setCancelled(true);
        Player player = (Player) event.getWhoClicked();
        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null || clickedItem.getType() == Material.AIR) return;
        if (clickedItem != null && clickedItem.getType() == Material.ENCHANTED_BOOK) {
            if (clickedItem.getItemMeta() != null && ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName()).equals("전직")) {
                event.setCancelled(true);
                openNewGui1((InventoryClickEvent) player);
            }
        }
        // 아이템 클릭에 따른 행동 정의
        if (clickedItem.getType() == Material.STONE) {
            player.sendMessage("You clicked on a Stone1!");
        }
    }
    @EventHandler
    public void openNewGui1(InventoryClickEvent event) {
        event.setCancelled(true);
        Player player = (Player) event.getWhoClicked();
        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null || clickedItem.getType() == Material.AIR) return;

        Inventory newInventory = Bukkit.createInventory(null, 45, "전직 상점");
        ItemStack itemStack = new ItemStack(Material.WHITE_STAINED_GLASS_PANE, 1);
        for (int i = 0; i < 9; i++) {
            newInventory.setItem(i, itemStack);
        }
        newInventory.setItem(9, itemStack);
        newInventory.setItem(17, itemStack);
        newInventory.setItem(18, itemStack);
        newInventory.setItem(26, itemStack);
        newInventory.setItem(27, itemStack);
        newInventory.setItem(35, itemStack);
        for (int i = 36; i < 45; i++) {
            newInventory.setItem(i, itemStack);
        }
        player.openInventory(newInventory);
    }
}
