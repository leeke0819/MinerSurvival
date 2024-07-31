package org.example.code.rpg.Event;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.example.code.rpg.Manager.GuiManager;

public class InventoryClickListener implements Listener {
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
                //여기서 elseif 계속 써가면서 커스텀아이템 검증하는 로직 작성
            }
            event.setCancelled(true);
        }
        // 아이템 클릭에 따른 행동 정의
        if (clickedItem.getType() == Material.STONE) {
            player.sendMessage("You clicked on a Stone1!");
        }
    }
}
