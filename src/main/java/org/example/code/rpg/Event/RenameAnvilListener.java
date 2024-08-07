package org.example.code.rpg.Event;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import static org.bukkit.Bukkit.getLogger;

public class RenameAnvilListener implements Listener {

    @EventHandler
    public void onPrepareAnvil(PrepareAnvilEvent event) {
        AnvilInventory anvilInventory = event.getInventory();
        ItemStack resultItem = anvilInventory.getItem(2); // 모루의 결과 아이템 슬롯 (슬롯 2번)

        if (resultItem != null && resultItem.hasItemMeta() && resultItem.getItemMeta().hasDisplayName()) {
            String newName = resultItem.getItemMeta().getDisplayName();

            // 특정 단어가 포함된 이름으로 변경을 막음
            if (newName.contains("광부 1차")||newName.contains("광부 2차")||newName.contains("광부 3차")||newName.contains("광부 4차")) {
                event.setResult(null);
                // 플레이어에게 메시지 보내기
                event.getView().getPlayer().sendMessage(ChatColor.RED + "이 아이템의 이름을 '광부'로 변경할 수 없습니다.");
            }
        }
    }
}
