package org.example.code.rpg.Command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class GuiCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "플레이어만 사용 가능한 명령어 입니다.");
            return true;
        }
        Player player = (Player) sender;
        openGui(player);
        return true;
    }

    public void openGui(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 54, "test");
        ItemStack itemStack = new ItemStack(Material.STONE, 1);
        inventory.setItem(0, itemStack);
        player.openInventory(inventory);
    }
}
