package org.example.code.rpg.Manager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GuiManager {
    public GuiManager() {}
    public void openGui(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 27, "메뉴");

        ItemStack itemStack = new ItemStack(Material.WHITE_STAINED_GLASS_PANE, 1);
        ItemStack itemStack1 = new ItemStack(Material.ENCHANTED_BOOK, 1);
        ItemStack itemStack2 = new ItemStack(Material.NETHERITE_PICKAXE, 1);
        ItemStack itemStack3 = new ItemStack(Material.PAPER, 1);
        ItemStack itemStack4 = new ItemStack(Material.WRITABLE_BOOK, 1);

        ItemMeta meta1 = itemStack1.getItemMeta();
        ItemMeta meta2 = itemStack2.getItemMeta();
        ItemMeta meta3 = itemStack3.getItemMeta();
        ItemMeta meta4 = itemStack4.getItemMeta();

        if (meta1 != null) {
            meta1.setDisplayName(ChatColor.YELLOW.toString() + ChatColor.BOLD + "전직");
            meta1.addItemFlags(ItemFlag.HIDE_ATTRIBUTES); // 태그 숨기기
            itemStack1.setItemMeta(meta1);
        }
        if (meta2 != null) {
            meta2.setDisplayName(ChatColor.GOLD.toString() + ChatColor.BOLD + "광물 판매");
            meta2.addEnchant(Enchantment.DURABILITY, 1, true); // 인챈트 부여
            meta2.addItemFlags(ItemFlag.HIDE_ENCHANTS); // 인챈트 숨기기
            meta2.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            itemStack2.setItemMeta(meta2);
        }
        if (meta3 != null) {
            meta3.setDisplayName(ChatColor.GRAY.toString() + ChatColor.BOLD + "단서");
            meta3.addEnchant(Enchantment.DURABILITY, 1, true); // 인챈트 부여
            meta3.addItemFlags(ItemFlag.HIDE_ENCHANTS); // 인챈트 숨기기
            meta3.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            itemStack3.setItemMeta(meta3);
        }
        if (meta4 != null) {
            meta4.setDisplayName(ChatColor.GREEN.toString() + ChatColor.BOLD + "도움말");
            meta4.addEnchant(Enchantment.DURABILITY, 1, true); // 인챈트 부여
            meta4.addItemFlags(ItemFlag.HIDE_ENCHANTS); // 인챈트 숨기기
            meta4.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            itemStack4.setItemMeta(meta4);
        }


        for(int i = 0; i < 9; i++) {
            inventory.setItem(i, itemStack);
        }
        inventory.setItem(9, itemStack);

        inventory.setItem(10, itemStack1);
        inventory.setItem(12, itemStack2);
        inventory.setItem(14, itemStack3);
        inventory.setItem(16, itemStack4);

        inventory.setItem(17, itemStack);
        for(int i = 18; i < 27; i++) {
            inventory.setItem(i, itemStack);
        }
        player.openInventory(inventory);
    }

    public void jobShop(Player player){
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
