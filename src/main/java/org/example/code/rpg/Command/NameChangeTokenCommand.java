package org.example.code.rpg.Command;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.example.code.rpg.RPG;

import java.util.Collections;

public class NameChangeTokenCommand implements CommandExecutor {
    private RPG plugin;

    public NameChangeTokenCommand(RPG plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "플레이어만 사용 가능한 명령어 입니다.");
            return true;
        }

        Player player = (Player) sender;
        ItemStack nameChangeToken = createNameChangeToken();
        player.getInventory().addItem(nameChangeToken);
        player.sendMessage(ChatColor.GREEN + "이름 변경권을 받았습니다!");

        return true;
    }

    private ItemStack createNameChangeToken() {
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "이름 변경권");
            meta.setLore(Collections.singletonList(ChatColor.RED + "변경한 이름은 다시 변경할 수 없습니다."));
            meta.addEnchant(Enchantment.DURABILITY, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
            item.setItemMeta(meta);
        }
        return item;
    }
}
