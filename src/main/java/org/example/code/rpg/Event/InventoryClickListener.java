package org.example.code.rpg.Event;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.example.code.rpg.Manager.GuiManager;
import org.example.code.rpg.Manager.JobConfigManager;
import org.example.code.rpg.Manager.MoneyManager;
import org.example.code.rpg.RPG;

import java.util.Objects;

public class InventoryClickListener implements Listener {
    private RPG plugin;
    public InventoryClickListener(RPG plugin) {
        this.plugin = plugin;
    }
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if(!event.getView().getTitle().equals("메뉴") && !event.getView().getTitle().equals("전직 상점")) return;
        event.setCancelled(true);
        Player player = (Player) event.getWhoClicked();
        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem != null){
            event.setCancelled(true);
            if (clickedItem.getType() == Material.ENCHANTED_BOOK){
                if (clickedItem.getItemMeta() != null && ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName()).equals("전직")) {
                    GuiManager guiManager = new GuiManager();
                    guiManager.openGui(player);
                }
                else if (clickedItem.getType() == Material.AIR) {
                    return;
                }
                else if (Objects.requireNonNull(clickedItem.getItemMeta()).getDisplayName().equals("광부 1차")) {
                    MoneyManager moneyManager = new MoneyManager(plugin);

                }
            }
            event.setCancelled(true);
        }
        // 아이템 클릭에 따른 행동 정의
        if (clickedItem.getType() == Material.STONE) {
            player.sendMessage("You clicked on a Stone1!");
        }
    }
}
