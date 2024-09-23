package org.example.code.rpg.Manager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.example.code.rpg.RPG;

import java.util.HashSet;
import java.util.Set;

public class NameChangeManager implements Listener {
    private RPG plugin;
    private Set<Player> nameChangeList = new HashSet<>();

    public NameChangeManager(RPG plugin) {
        this.plugin = plugin;
    }

    public void addPlayerToNameChangeList(Player player) {
        nameChangeList.add(player);
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (nameChangeList.contains(player)) {
            event.setCancelled(true);
            String newName = event.getMessage().trim();

            Bukkit.getScheduler().runTask(plugin, () -> {
                FileConfiguration config = plugin.getConfig();
                config.set("users." + player.getUniqueId().toString() + ".name", newName);
                plugin.saveConfig();

                player.sendMessage(ChatColor.GREEN + "이름이 성공적으로 " + ChatColor.YELLOW + newName + ChatColor.GREEN + "(으)로 변경되었습니다!");

                // 스코어보드 업데이트
                plugin.getScoreboardManager().setPlayerScoreboard(player);

                // 이름 변경하기(플레이어 위에 뜨는 마인크래프트 고유 닉네임, Tab 누르면 뜨는 플레이어 목록 리스트에 뜨는 닉네임)
                player.setDisplayName(newName);
                player.setPlayerListName(newName);

                // 사용 이후 인벤토리에서 삭제
                ItemStack itemInHand = player.getInventory().getItemInMainHand();
                if (itemInHand != null && itemInHand.getType() == Material.PAPER && itemInHand.hasItemMeta()) {
                    ItemMeta meta = itemInHand.getItemMeta();
                    if (meta != null && ChatColor.stripColor(meta.getDisplayName()).equals("이름 변경권")) {
                        itemInHand.setAmount(itemInHand.getAmount() - 1);
                    }
                }

                // 리스트에서 플레이어 제거
                nameChangeList.remove(player);
            });
        }
    }
}
